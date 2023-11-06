package com.negongal.hummingbird.domain.artist.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.negongal.hummingbird.domain.artist.domain.Artist;
import com.negongal.hummingbird.domain.artist.domain.ArtistHeart;
import java.util.ArrayList;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import org.springframework.transaction.annotation.Transactional;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ArtistDto {
    private String id;

    private String name;

    private String image;

    private List<String> genres;

    private int popularity;

    private List<ArtistHeart> artistHearts;

    @Builder
    public ArtistDto(String id, String name, String image, List<String> genres, int popularity,
                     List<ArtistHeart> artistHearts) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.genres = genres;
        this.popularity = popularity;
        this.artistHearts = artistHearts;
    }

    public static ArtistDto of(Artist artist) {
        return ArtistDto.builder()
                .id(artist.getId())
                .name(artist.getName())
                .image(artist.getImage())
                .genres(artist.getGenreList())
                .popularity(artist.getPopularity())
                .artistHearts(artist.getArtistHeartList())
                .build();
    }

}
