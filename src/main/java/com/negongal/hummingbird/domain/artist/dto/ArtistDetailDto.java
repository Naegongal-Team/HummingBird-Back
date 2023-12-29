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
public class ArtistDetailDto {
    private String id;

    private String name;

    private String image;

    private List<ArtistGenresDto> genres = new ArrayList<>();

    private List<TrackDto> topTracks;

    private List<ArtistHeart> artistHearts;

    private boolean isHearted;

    @Builder
    public ArtistDetailDto(String id, String name, String image, List<ArtistGenresDto> genres,
                           List<TrackDto> topTracks, List<ArtistHeart> artistHearts, boolean isHearted) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.genres = genres;
        this.topTracks = topTracks;
        this.artistHearts = artistHearts;
        this.isHearted = isHearted;
    }

    public static ArtistDetailDto of(Artist artist, boolean isHearted) {
        List<ArtistGenresDto> artistGenres = artist.getGenreList().stream().map(genre ->
                ArtistGenresDto.builder()
                        .genres(genre.getGenreName())
                        .build()).collect(Collectors.toList());
        return ArtistDetailDto.builder()
                .id(artist.getId())
                .name(artist.getName())
                .image(artist.getImage())
                .genres(artistGenres)
                .topTracks(artist.getArtistTopTrackList().stream()
                        .map(track -> TrackDto.of(track))
                        .collect(Collectors.toList()))
                .isHearted(isHearted)
                .build();
    }
}
