package com.negongal.hummingbird.domain;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
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

    @Column(nullable = false)
    private String location;

    @Column(nullable = false)
    private Long runtime;

    @Column(length = 1000)
    private String photo;

    private String description;

    @OneToMany(mappedBy = "performance", orphanRemoval = true)
    private List<PerformanceHeart> heartList;

    @OneToMany(mappedBy = "performance", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PerformanceDate> dateList;

    @OneToMany(mappedBy = "performance", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Ticketing> ticketingList;

    @Builder
    public Performance(String name, String artistName, String location, Long runtime, String description) {
        this.name = name;
        this.artistName = artistName;
        this.location = location;
        this.runtime = runtime;
        this.description = description;
        this.ticketingList = new ArrayList<>();
        this.dateList = new ArrayList<>();
        this.heartList = new ArrayList<>();
    }

    public void addPhoto(String photo) {
        this.photo = photo;
    }

    public void update(String name, String artistName, String location, Long runtime, String description) {
        this.name = name;
        this.artistName = artistName;
        this.location = location;
        this.runtime = runtime;
        this.description = description;

        this.photo = null;
        this.ticketingList.clear();
        this.dateList.clear();
    }

}
