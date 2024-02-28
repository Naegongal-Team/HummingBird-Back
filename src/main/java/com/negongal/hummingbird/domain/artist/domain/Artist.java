package com.negongal.hummingbird.domain.artist.domain;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.negongal.hummingbird.domain.performance.domain.Performance;
import com.negongal.hummingbird.global.common.BaseTimeEntity;
import java.util.ArrayList;
import lombok.*;

import javax.persistence.*;
import java.util.List;
import org.hibernate.annotations.Formula;
import org.springframework.data.domain.Persistable;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "ARTIST", indexes = @Index(name = "idx_artist_name", columnList = "name", unique = true))
@Entity
public class Artist extends BaseTimeEntity implements Persistable<String> {
    @Id
    private String id;

    @Column(nullable = false)
    private String name;

    private String image;

    private String nickname;

    @Basic(fetch=FetchType.LAZY)
    @Formula("(SELECT COUNT(1) FROM artist_heart ah WHERE ah.artist_id = id)")
    private Integer heartCount;

    @OneToMany(mappedBy = "artist", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Genre> genres;

    @OneToMany(mappedBy = "artist", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ArtistHeart> hearts;

    @OneToMany(mappedBy = "artist", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Track> topTracks;

    @OneToMany(mappedBy = "artist", cascade = CascadeType.ALL)
    private List<Performance> performances;

    @Builder
    public Artist(String id, String name, String image, List<Genre> genres, List<Track> topTracks) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.genres = genres;
        this.topTracks = topTracks;
        this.performances = new ArrayList<>();
        this.hearts = new ArrayList<>();
        this.heartCount = 0;
    }

    public void updateNickname(String nickname) {
        this.nickname = nickname;
    }

    @Override
    public boolean isNew() {
        return this.getCreatedDate() == null;
    }
}
