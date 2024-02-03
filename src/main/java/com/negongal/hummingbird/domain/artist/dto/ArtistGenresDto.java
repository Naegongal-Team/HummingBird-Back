package com.negongal.hummingbird.domain.artist.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ArtistGenresDto {
    private String name;

    @Builder
    @QueryProjection
    public ArtistGenresDto(String name) {
        this.name = name;
    }
}
