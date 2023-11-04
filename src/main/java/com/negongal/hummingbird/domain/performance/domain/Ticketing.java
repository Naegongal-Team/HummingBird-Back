package com.negongal.hummingbird.domain.performance.domain;

import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
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

@Entity
@Table
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Ticketing {

    @Id @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "performance_id")
    private Performance performance;

    @Enumerated(EnumType.STRING)
    private TicketType ticketType;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startDate;

    private String platform;
    private String link;

    @Builder
    public Ticketing(Performance performance, TicketType ticketType, LocalDateTime startDate, String platform, String link) {
        this.performance = performance;
        this.ticketType = ticketType;
        this.startDate = startDate;
        this.platform = platform;
        this.link = link;

        //==연관관계 편의 메서드==//
        performance.getTicketingList().add(this);
    }
}
