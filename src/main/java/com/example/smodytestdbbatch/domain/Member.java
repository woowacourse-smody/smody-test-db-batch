package com.example.smodytestdbbatch.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.DynamicUpdate;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(uniqueConstraints = {@UniqueConstraint(name = "unique_column_in_member", columnNames = "email")})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DynamicUpdate
@Getter
public class Member {

    private static final String DEFAULT_INTRODUCTION = "스모디로 작심삼일 시작!";

    @Id
    @Column(name = "member_id")
    private Long id;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String nickname;

    @Column(length = 30)
    private String introduction;

    @Column(nullable = false)
    private String picture;

    public Member(String email, String nickname, String introduction, String picture) {
        this.email = email;
        this.nickname = nickname;
        this.introduction = introduction;
        this.picture = picture;
    }

    public Member(String email, String nickname, String picture) {
        this(email, nickname, DEFAULT_INTRODUCTION, picture);
    }
}
