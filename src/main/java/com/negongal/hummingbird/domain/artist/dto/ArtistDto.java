package com.negongal.hummingbird.domain.artist.dto;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.negongal.hummingbird.domain.artist.domain.Artist;
import com.negongal.hummingbird.domain.artist.domain.ArtistHeart;
import com.querydsl.core.annotations.QueryProjection;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class ArtistDto {
    private String id;

    private String name;

    private String image;

    private int heartCount;

    @Builder
    @QueryProjection
    public ArtistDto(String id, String name, String image, int heartCount) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.heartCount = heartCount;
    }

    public static ArtistDto of(Artist artist) {
        return ArtistDto.builder()
                .id(artist.getId())
                .name(artist.getName())
                .image(artist.getImage())
                .heartCount(artist.getHeartCount())
                .build();
    }

}
