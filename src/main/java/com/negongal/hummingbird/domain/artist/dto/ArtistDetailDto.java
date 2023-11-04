package com.negongal.hummingbird.domain.artist.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.negongal.hummingbird.domain.artist.domain.Artist;
import com.negongal.hummingbird.domain.artist.domain.ArtistHeart;
import com.negongal.hummingbird.domain.performance.dto.PerformanceDto;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class ArtistDetailDto {
    private String id;

    private String name;

    private String image;

    private List<String> genres = new ArrayList<>();

    private List<TrackDto> topTracks;

    private List<PerformanceDto> performances;

    private List<ArtistHeart> artistHearts;

    @Builder
    public ArtistDetailDto(String id, String name, String image, List<String> genres, List<PerformanceDto> performances,
                           List<TrackDto> topTracks, List<ArtistHeart> artistHearts) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.genres = genres;
        this.performances = performances;
        this.topTracks = topTracks;
        this.artistHearts = artistHearts;
    }

    public static ArtistDetailDto of(Artist artist) {
        return ArtistDetailDto.builder()
                .id(artist.getId())
                .name(artist.getName())
                .image(artist.getImage())
                .genres(artist.getGenreList())
                .topTracks(artist.getArtistTopTrackList().stream()
                        .map(track -> TrackDto.of(track))
                        .collect(Collectors.toList()))
                .performances(artist.getPerformanceList().stream()
                        .map(performance -> PerformanceDto.of(performance))
                        .collect(Collectors.toList()))
                .build();
    }
}
