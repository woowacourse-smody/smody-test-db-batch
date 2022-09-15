package com.example.smodytestdbbatch;

import com.example.smodytestdbbatch.domain.Challenge;
import com.example.smodytestdbbatch.domain.Comment;
import com.example.smodytestdbbatch.domain.Cycle;
import com.example.smodytestdbbatch.domain.CycleDetail;
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

    @Transactional
    public void setUp() {
        batchInsert(100, 100, 50, 2);
    }

    private void batchInsert(int memberCount, int challengeCount, int cycleCount, int commentCount) {
        List<Member> members = insertMember(memberCount);
        List<Challenge> challenges = insertChallenge(challengeCount);

        ArrayList<Cycle> cycles = new ArrayList<>();
        List<CycleDetail> cycleDetails = new ArrayList<>();
        List<Comment> comments = new ArrayList<>();

        long nextCycleId = 1L;
        long nextCycleDetailId = 1L;
        long nextCommentId = 1L;

        for (Member member : members) {
            for (Challenge challenge : challenges) {
                for (int i = 0; i < cycleCount; i++) {

                    int progressIndex = i % 4;
                    Progress value = Progress.values()[progressIndex];
                    Cycle cycle = new Cycle(member, challenge, value, timeOf(progressIndex));
                    cycles.add(cycle);

                    LocalDateTime st = cycle.getStartTime();
                    for (int j = 0; j < value.getCount(); j++) {
                        CycleDetail cycleDetail = new CycleDetail(cycle, st.plusDays(j).plusSeconds(1L), "image", "desc");
                        cycleDetails.add(cycleDetail);

                        for (int k = 0; k < commentCount; k++) {
                            comments.add(new Comment(cycleDetail, member, "content"));
                        }
                    }
                }
            }
            if (cycles.size() == 10000) {
                nextCycleId = BatchInsertSql.CYCLE.execute(em, cycles, nextCycleId) + 1; // 10000 - 50만
                nextCycleDetailId = BatchInsertSql.CYCLE_DETAIL.execute(em, cycleDetails, nextCycleDetailId) + 1; // 15000 - 75만
                nextCommentId = BatchInsertSql.COMMENT.execute(em, comments, nextCommentId) + 1; // 30000 - 150만
                cycles.clear();
                cycleDetails.clear();
                comments.clear();
            }
        }
    }

    private List<Member> insertMember(int count) {
        List<Member> members = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            Member member = new Member("email" + i + "@email.com",
                    "nickname" + i, "intro" + i, "picture" + i);
            members.add(member);
        }
        BatchInsertSql.MEMBER.execute(em, members, 1L);
        return members;
    }

    private List<Challenge> insertChallenge(int count) {
        List<Challenge> challenges = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            Challenge challenge = new Challenge("challenge" + i, "description" + i, i, i);
            challenges.add(challenge);
        }
        BatchInsertSql.CHALLENGE.execute(em, challenges, 1L);
        return challenges;
    }

    private LocalDateTime timeOf(int progressIndex) {
        return LocalDateTime.now()
                .minusDays(progressIndex * 3L);
    }
}
