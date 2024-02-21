package com.negongal.hummingbird.domain.artist.domain;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.negongal.hummingbird.domain.user.domain.User;
import com.negongal.hummingbird.global.common.BaseTimeEntity;
import lombok.*;

import javax.persistence.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class ArtistHeart extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Boolean isAlarmed;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "artist_id")
    private Artist artist;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public void toggleAlarmed() {
        isAlarmed = !isAlarmed;
    }

    @Builder
    public ArtistHeart(Artist artist, User user) {
        this.isAlarmed = false;
        this.artist = artist;
        this.user = user;
    }
}
