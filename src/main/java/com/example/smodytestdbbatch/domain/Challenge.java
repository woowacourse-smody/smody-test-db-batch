package com.example.smodytestdbbatch.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(uniqueConstraints = {@UniqueConstraint(name = "unique_column_in_challenge", columnNames = "name")})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Challenge {

    @Id
    @Column(name = "challenge_id")
    private Long id;

    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private Integer emojiIndex;

    @Column(nullable = false)
    private Integer colorIndex;

    @Column(nullable = false)
    private String description;

    public Challenge(String name) {
        this.name = name;
    }

    public Challenge(String name, String description, Integer emojiIndex, Integer colorIndex) {
        this.name = name;
        this.description = description;
        this.emojiIndex = emojiIndex;
        this.colorIndex = colorIndex;
    }
}
