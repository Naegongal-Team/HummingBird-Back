package com.negongal.hummingbird.domain.performance.domain;

import com.negongal.hummingbird.domain.artist.domain.Artist;
import com.negongal.hummingbird.domain.chat.domain.ChatRoom;
import com.negongal.hummingbird.global.common.BaseTimeEntity;
import java.util.ArrayList;
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
import javax.persistence.OneToOne;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Performance extends BaseTimeEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "artist_id")
    private Artist artist;

    @Column(nullable = false)
    private String location;

    @Column(nullable = false)
    private Long runtime;

    @Column(length = 1000)
    private String photo;

    private String description;

    @OneToMany(mappedBy = "performance", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PerformanceHeart> performanceHearts;

    @OneToMany(mappedBy = "performance", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PerformanceDate> performanceDates;

    @OneToMany(mappedBy = "performance", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Ticketing> ticketings;

    @OneToOne(mappedBy = "performance", cascade = CascadeType.ALL, orphanRemoval = true)
    private ChatRoom chatRoom;

    @Builder
    public Performance(String name, Artist artist, String location, Long runtime, String description, String photo) {
        this.name = name;
        this.artist = artist;
        this.location = location;
        this.runtime = runtime;
        this.description = description;
        this.photo = photo;
        this.ticketings = new ArrayList<>();
        this.performanceDates = new ArrayList<>();
        this.performanceHearts = new ArrayList<>();

        this.artist.getPerformances().add(this);
    }

    public void update(String name, Artist artist, String location, Long runtime, String description, String photo) {
        this.name = name;
        this.artist = artist;
        this.location = location;
        this.runtime = runtime;
        this.description = description;
        this.photo = photo;

        this.ticketings.clear();
        this.performanceDates.clear();
        this.artist.getPerformances().add(this);
    }

}
