package com.negongal.hummingbird.domain.artist.domain;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.negongal.hummingbird.domain.performance.domain.Performance;
import lombok.*;

import javax.persistence.*;
import java.util.List;
import org.hibernate.annotations.Formula;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@Builder
@Getter
@ToString
public class Artist {
    @Id
    private String id;

    @Column(nullable = false, unique = true)
    private String name;

    private String image;

    private int popularity;

    @Formula("(SELECT COUNT(1) FROM artist_heart ah WHERE ah.artist_id = id)")
    private int heartCount;

    @OneToMany(mappedBy = "artist", orphanRemoval = true)
    private List<Genre> genreList;

    @OneToMany(mappedBy = "artist", orphanRemoval = true)
    private List<ArtistHeart> artistHeartList;

    @OneToMany(mappedBy = "artist", orphanRemoval = true)
    private List<Track> artistTopTrackList;

    @OneToMany(mappedBy = "artist")
    private List<Performance> performanceList;


}
