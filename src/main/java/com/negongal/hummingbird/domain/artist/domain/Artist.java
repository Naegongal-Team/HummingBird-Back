package com.negongal.hummingbird.domain.artist.domain;

import com.negongal.hummingbird.domain.performance.domain.Performance;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
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

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "GENRES", joinColumns = @JoinColumn(name = "artist_id"))
    private List<String> genres;

    @OneToMany(mappedBy = "artist", orphanRemoval = true)
    private List<ArtistHeart> artistHearts;

    @OneToMany(mappedBy = "artist", orphanRemoval = true)
    private List<Track> artistTopTracks;

    @OneToMany(mappedBy = "artist")
    private List<Performance> performances;
}
