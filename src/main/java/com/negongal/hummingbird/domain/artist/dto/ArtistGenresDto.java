package com.negongal.hummingbird.domain.artist.dto;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.querydsl.core.annotations.QueryProjection;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ArtistGenresDto {
    private String genres;


    @Builder
    @QueryProjection
    public ArtistGenresDto(String genres, String artistId) {
        this.genres = genres;
    }
}
