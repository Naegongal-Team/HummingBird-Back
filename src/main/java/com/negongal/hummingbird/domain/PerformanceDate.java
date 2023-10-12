package com.negongal.hummingbird.domain;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

@Getter
@Table
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PerformanceDate {
    @Id @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "performance_pk")
    private Performance performance;

    @Column(nullable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime date;

    @Builder
    public PerformanceDate(Performance performance, LocalDateTime date) {
        this.performance = performance;
        this.date = date;

        //==연관관계 편의 메서드==//
        performance.getDate().add(this);
    }
}