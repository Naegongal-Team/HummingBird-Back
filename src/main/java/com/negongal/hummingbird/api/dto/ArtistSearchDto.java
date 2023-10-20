package com.negongal.hummingbird.api.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class ArtistSearchDto {
    private String id;
    private String name;

    @Builder
    public ArtistSearchDto(String id, String name) {
        this.id = id;
        this.name = name;
    }
}
