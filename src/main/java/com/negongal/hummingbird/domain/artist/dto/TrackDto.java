package com.negongal.hummingbird.domain.artist.dto;

import com.negongal.hummingbird.domain.artist.domain.Artist;
import com.negongal.hummingbird.domain.artist.domain.Track;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TrackDto {
    private Long id;

    private String name;

    private String albumName;

    private String releaseDate;

    private String albumImage;

    @Builder
    public TrackDto(Long id, String name, String albumName, String albumImage, String releaseDate, Artist artist) {
        this.id = id;
        this.name = name;
        this.albumName = albumName;
        this.releaseDate = releaseDate;
        this.albumImage = albumImage;
    }

    public static TrackDto of(Track track) {
        return TrackDto.builder()
                .id(track.getId())
                .name(track.getName())
                .albumName(track.getAlbumName())
                .albumImage(track.getAlbumImage())
                .releaseDate(track.getReleaseDate())
                .build();
    }
}
