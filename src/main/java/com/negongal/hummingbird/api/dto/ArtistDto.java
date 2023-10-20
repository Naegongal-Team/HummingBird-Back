package com.negongal.hummingbird.api.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.negongal.hummingbird.domain.Artist;
import com.negongal.hummingbird.domain.ArtistLike;
import com.negongal.hummingbird.domain.Track;
import com.negongal.hummingbird.service.ArtistLikeService;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class ArtistDto {
    private String id;

    private String name;

    private String image;

    private String genres;

    private int popularity;

    private List<ArtistLike> artistLikes;

    private List<TrackDto> artistTopTracks;

    @Builder
    public ArtistDto(String id, String name, String image, String genres, int popularity, List<ArtistLike> artistLikes, List<TrackDto> artistTopTracks) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.genres = genres;
        this.popularity = popularity;
        this.artistLikes = artistLikes;
        this.artistTopTracks = artistTopTracks;
    }

    public static ArtistDto of(Artist artist) {
        return ArtistDto.builder()
                .id(artist.getId())
                .name(artist.getName())
                .image(artist.getImage())
                .genres(artist.getGenres())
                .popularity(artist.getPopularity())
                .artistLikes(artist.getArtistLikes())
                .artistTopTracks(artist.getArtistTopTracks().stream()
                        .map(track -> TrackDto.of(track))
                        .collect(Collectors.toList()))
                .build();
    }

}
