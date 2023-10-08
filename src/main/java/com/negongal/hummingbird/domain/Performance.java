package com.negongal.hummingbird.domain;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Performance {
    @Id @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private String name;

    private String artistName;  /** Artist 매핑 필요 **/

    private String photo;

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
    public Performance(String name, String artistName, String location, Long runtime, LocalDateTime date) {
        this.name = name;
        this.artistName = artistName;
        this.location = location;
        this.runtime = runtime;
        this.date = date;
    }

    public void addPhoto(String photo) {
        this.photo = photo;
    }

    public void addTicketingData(String ticketingLink, LocalDateTime ticketingDate) {
        this.ticketingLink = ticketingLink;
        this.ticketingDate = ticketingDate;
    }

    public void addDescription(String description) {
        this.description = description;
    }

}
