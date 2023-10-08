package com.negongal.hummingbird.domain;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Builder;
import lombok.Getter;

@Entity
@Table
@Getter
public class Performance {
    @Id @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String location;

    @Column(nullable = false)
    private Long runtime;

    @Column(nullable = false)
    private LocalDateTime date;

    private String description;
    private String ticketingLink;
    private LocalDateTime ticketingDate;

    @Builder(builderMethodName = "createPerformanceBuilder")
    public Performance(String name, String location, Long runtime, LocalDateTime date) {
        this.name = name;
        this.location = location;
        this.runtime = runtime;
        this.date = date;
    }

    public void addTicketingData(String ticketingLink, LocalDateTime ticketingDate) {
        this.ticketingLink = ticketingLink;
        this.ticketingDate = ticketingDate;
    }

    public void addDescription(String description) {
        this.description = description;
    }

}
