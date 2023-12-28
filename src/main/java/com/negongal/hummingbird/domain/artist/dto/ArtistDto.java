package com.negongal.hummingbird.domain.artist.dto;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.negongal.hummingbird.domain.artist.domain.Artist;
import com.querydsl.core.annotations.QueryProjection;
import java.util.stream.Collectors;
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

    private List<ArtistGenresDto> genres;

    private int heartCount;

    @Builder
    @QueryProjection
    public ArtistDto(String id, String name, String image, int heartCount, List<ArtistGenresDto> genres) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.heartCount = heartCount;
        this.genres = genres;
    }

    public static ArtistDto of(Artist artist) {
        List<ArtistGenresDto> genresDto = artist.getGenreList().stream().map(
                genre -> {
                    ArtistGenresDto dto = ArtistGenresDto.builder()
                            .genres(genre.getGenreName())
                            .artistId(artist.getId())
                            .build();
                    return dto;
                })
                .collect(Collectors.toList());
        return ArtistDto.builder()
                .id(artist.getId())
                .name(artist.getName())
                .image(artist.getImage())
                .heartCount(artist.getHeartCount())
                .genres(genresDto)
                .build();
    }

}
