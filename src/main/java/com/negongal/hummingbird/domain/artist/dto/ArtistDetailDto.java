package com.negongal.hummingbird.domain.artist.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.negongal.hummingbird.domain.artist.domain.Artist;
import com.negongal.hummingbird.domain.artist.domain.ArtistHeart;

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

	private Integer heartCount;

	private boolean isHearted;

	private boolean isAlarmed;

	@Builder
	public ArtistDetailDto(String id, String name, String image, List<ArtistGenresDto> genres,
		List<TrackDto> topTracks, Integer heartCount, List<ArtistHeart> artistHearts,
		boolean isHearted, boolean isAlarmed) {
		this.id = id;
		this.name = name;
		this.image = image;
		this.genres = genres;
		this.topTracks = topTracks;
		this.artistHearts = artistHearts;
		this.isHearted = isHearted;
		this.isAlarmed = isAlarmed;
		this.heartCount = heartCount;
	}

	public static ArtistDetailDto of(Artist artist, boolean isHearted, boolean isAlarmed) {
		List<ArtistGenresDto> artistGenres = artist.getGenres().stream().map(genre ->
			ArtistGenresDto.builder()
				.name(genre.getName())
				.build()).collect(Collectors.toList());
		return ArtistDetailDto.builder()
			.id(artist.getId())
			.name(artist.getName())
			.image(artist.getImage())
			.genres(artistGenres)
			.heartCount(artist.getHeartCount())
			.topTracks(artist.getTopTracks().stream()
				.map(track -> TrackDto.of(track))
				.collect(Collectors.toList()))
			.isHearted(isHearted)
			.isAlarmed(isAlarmed)
			.build();
	}
}
