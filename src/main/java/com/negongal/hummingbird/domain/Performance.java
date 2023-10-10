package com.negongal.hummingbird.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
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

    @Column(nullable = false)
    private LocalDateTime date;

    private String photo;
    private String description;

    @OneToMany(mappedBy = "performance")
    private List<Ticketing> ticketing;

    @Builder
    public Performance(String name, String artistName, String location, Long runtime, String description, LocalDateTime date) {
        this.name = name;
        this.artistName = artistName;
        this.location = location;
        this.runtime = runtime;
        this.date = date;
        this.description = description;
        this.ticketing = new ArrayList<>();
    }

    public void addPhoto(String photo) {
        this.photo = photo;
    }


}
