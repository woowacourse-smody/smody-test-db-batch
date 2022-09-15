package com.example.smodytestdbbatch;

import com.example.smodytestdbbatch.domain.Challenge;
import com.example.smodytestdbbatch.domain.Comment;
import com.example.smodytestdbbatch.domain.Cycle;
import com.example.smodytestdbbatch.domain.CycleDetail;
import com.example.smodytestdbbatch.domain.Member;
import java.lang.reflect.Field;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.EntityManager;

public enum BatchInsertSql {
    MEMBER {
        @Override
        public <T> long execute(EntityManager em, List<T> entities, long startId) {
            String values = entities.stream()
                    .map(entity -> (Member) entity)
                    .map(entity -> String.format("('%s', '%s', '%s', '%s')",
                            entity.getEmail(),
                            entity.getNickname(),
                            entity.getIntroduction(),
                            entity.getPicture())
                    )
                    .collect(Collectors.joining(", "));
            String sql = "insert into member (email, nickname, introduction, picture) values " + values + ";";
            em.createNativeQuery(sql).executeUpdate();
            return autoIncrement(entities, startId);
        }
    },
    CHALLENGE {
        @Override
        public <T> long execute(EntityManager em, List<T> entities, long startId) {
            String values = entities.stream()
                    .map(entity -> (Challenge) entity)
                    .map(entity -> String.format("('%s', '%s', %s, %s)",
                            entity.getName(),
                            entity.getDescription(),
                            entity.getEmojiIndex(),
                            entity.getColorIndex())
                    )
                    .collect(Collectors.joining(", "));
            String sql = "insert into challenge (name, description, emoji_index, color_index) values " + values + ";";
            em.createNativeQuery(sql).executeUpdate();
            return autoIncrement(entities, startId);
        }
    },
    CYCLE {
        @Override
        public <T> long execute(EntityManager em, List<T> entities, long startId) {
            String values = entities.stream()
                    .map(entity -> (Cycle) entity)
                    .map(entity -> String.format("(%s, %s, '%s', '%s')",
                            entity.getMember().getId(),
                            entity.getChallenge().getId(),
                            entity.getProgress(),
                            entity.getStartTime())
                    )
                    .collect(Collectors.joining(", "));

            String sql = "insert into cycle (member_id, challenge_id, progress, start_time) values " + values + ";";
            em.createNativeQuery(sql).executeUpdate();
            return autoIncrement(entities, startId);
        }
    },
    CYCLE_DETAIL {
        @Override
        public <T> long execute(EntityManager em, List<T> entities, long startId) {
            String values = entities.stream()
                    .map(entity -> (CycleDetail) entity)
                    .map(entity -> String.format("(%s, '%s', '%s', '%s')",
                            entity.getCycle().getId(),
                            entity.getProgressTime(),
                            entity.getProgressImage(),
                            entity.getDescription())
                    )
                    .collect(Collectors.joining(", "));

            String sql = "insert into cycle_detail (cycle_id, progress_time, progress_image, description) values " + values + ";";
            em.createNativeQuery(sql).executeUpdate();
            return autoIncrement(entities, startId);
        }
    },
    COMMENT {
        @Override
        public <T> long execute(EntityManager em, List<T> entities, long startId) {
            String values = entities.stream()
                    .map(entity -> (Comment) entity)
                    .map(entity -> String.format("(%s, %s, '%s')",
                            entity.getCycleDetail().getId(),
                            entity.getMember().getId(),
                            entity.getContent()
                    ))
                    .collect(Collectors.joining(", "));

            String sql = "insert into comment (cycle_detail_id, member_id, content) values " + values + ";";
            em.createNativeQuery(sql).executeUpdate();
            return autoIncrement(entities, startId);
        }
    };

    public abstract <T> long execute(EntityManager em, List<T> entities, long startId);

    protected  <T> long autoIncrement(List<T> entities, long startId) {
        for (T entity : entities) {
            Field id;
            try {
                id = entity.getClass().getDeclaredField("id");
                id.setAccessible(true);
                id.set(entity, startId++);
            } catch (NoSuchFieldException | IllegalAccessException e) {
                throw new RuntimeException("리플렉션 예외");
            }
        }
        return startId - 1;
    }
}
