package com.negongal.hummingbird.domain.artist.domain;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED) @AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder @Getter @ToString
public class Artist {

        @Id
        private String id;

        @Column(nullable = false, unique = true)
        private String name;

        private String image;

        private String genres;

        private int popularity;

        @OneToMany(mappedBy = "artist")
        private List<ArtistLike> artistLikes;

        @OneToMany(mappedBy = "artist")
        private List<Track> artistTopTracks;
}
