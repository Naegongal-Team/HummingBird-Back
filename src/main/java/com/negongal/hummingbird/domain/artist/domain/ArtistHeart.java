package com.negongal.hummingbird.domain.artist.domain;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.negongal.hummingbird.domain.user.domain.User;
import com.negongal.hummingbird.global.common.BaseTimeEntity;
import lombok.*;

import javax.persistence.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@Builder
@Getter
@ToString
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


    public void updateAlarmed() {
        if (isAlarmed)  {
            isAlarmed = false;
            return;
        }
        isAlarmed = true;
    }
}
