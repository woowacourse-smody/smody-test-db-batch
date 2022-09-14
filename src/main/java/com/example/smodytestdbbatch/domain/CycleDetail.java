package com.example.smodytestdbbatch.domain;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class CycleDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cycle_detail_id")
    private Long id;

    @JoinColumn(name = "cycle_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Cycle cycle;

    @OneToMany(mappedBy = "cycleDetail", cascade = {CascadeType.REMOVE}, fetch = FetchType.LAZY)
    private List<Comment> comments;

    @Column(nullable = false)
    private LocalDateTime progressTime;

    @Column(nullable = false)
    private String progressImage;

    @Column(nullable = false)
    private String description;

    public CycleDetail(Cycle cycle, LocalDateTime progressTime, String progressImage, String description) {
        this.cycle = cycle;
        this.progressTime = progressTime;
        this.progressImage = progressImage;
        this.description = description;
    }
}
