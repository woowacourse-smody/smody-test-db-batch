package com.example.smodytestdbbatch;

import com.example.smodytestdbbatch.domain.Challenge;
import com.example.smodytestdbbatch.domain.Member;
import com.example.smodytestdbbatch.domain.Progress;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class DataBatch {

    private final EntityManager em;
    private final List<Member> members = new ArrayList<>();
    private final List<Challenge> challenges = new ArrayList<>();

    @Transactional
    public void setUp() {
        List<String> memberList = new ArrayList<>();
        List<String> challengeList = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            Member member = new Member("email" + i + "@email.com",
                    "nickname" + i, "intro" + i, "picture" + i);
            memberList.add(String.format("(%s, '%s', '%s', '%s', '%s')", i, "email" + i + "@email.com",
                    "nickname" + i, "intro" + i, "picture" + i));
            members.add(member);

            Challenge challenge = new Challenge("challenge" + i, "description" + i, i, i);
            challengeList.add(String.format("(%s, '%s', '%s', %s, %s)", i,"challenge" + i, "description" + i, i, i));
            challenges.add(challenge);
        }
        batchInsert("member", "(member_id, email, nickname, introduction, picture)", memberList);
        batchInsert("challenge", "(challenge_id, name, description, emoji_index, color_index)", challengeList);

        int id = 0;
        List<String> cycleList = new ArrayList<>();
        for (Member member : members) {
            for (Challenge challenge : challenges) {
                for (int i = 0; i < 50; i++) {
                    int progressIndex = i % 4;
                    addSyntax(cycleList,
                            id++,
                            member.getId(),
                            challenge.getId(),
                            Progress.values()[progressIndex],
                            timeOf(progressIndex)
                    );
                }
            }
            if (cycleList.size() == 10000) {
                batchInsert(
                        "cycle",
                        "(cycle_id, member_id, challenge_id, progress, start_time)",
                        cycleList
                );
            }
        }
    }

    private void addSyntax(List<String> syntaxList, Object... column) {
        syntaxList.add(String.format("(%s, %s, %s, '%s', '%s')", column));
    }

    private void batchInsert(String entity, String format, List<String> syntaxList) {
        String sql = "insert into " + entity + " " + format + " values " +
                String.join(", ", syntaxList) +
                ";";
        em.createNativeQuery(sql).executeUpdate();
        syntaxList.clear();
    }

    private LocalDateTime timeOf(int progressIndex) {
        return LocalDateTime.now()
                .minusDays(progressIndex * 3L);
    }
}
