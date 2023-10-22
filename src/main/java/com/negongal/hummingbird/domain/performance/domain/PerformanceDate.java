package com.negongal.hummingbird.domain.performance.domain;

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
    @JoinColumn(name = "performance_id")
    private Performance performance;

    @Column(nullable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startDate;

    @Builder
    public PerformanceDate(Performance performance, LocalDateTime startDate) {
        this.performance = performance;
        this.startDate = startDate;

        //==연관관계 편의 메서드==//
        performance.getDateList().add(this);
    }
}
