package com.negongal.hummingbird.domain.artist.domain;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.negongal.hummingbird.domain.performance.domain.Performance;
import lombok.*;

import javax.persistence.*;
import java.util.List;
import org.hibernate.annotations.Formula;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Artist {
    @Id
    private String id;

    @Column(nullable = false, unique = true)
    private String name;

    private String image;

    @Basic(fetch=FetchType.LAZY)
    @Formula("(SELECT COUNT(1) FROM artist_heart ah WHERE ah.artist_id = id)")
    private int heartCount;

    @OneToMany(mappedBy = "artist", orphanRemoval = true)
    private List<Genre> genres;

    @OneToMany(mappedBy = "artist", orphanRemoval = true)
    private List<ArtistHeart> hearts;

    @OneToMany(mappedBy = "artist", orphanRemoval = true)
    private List<Track> topTracks;

    @OneToMany(mappedBy = "artist")
    private List<Performance> performances;

    @Builder
    public Artist(String id, String name, String image, List<Genre> genres, List<Track> topTracks) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.genres = genres;
        this.topTracks = topTracks;
    }
}
