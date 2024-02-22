package com.negongal.hummingbird.domain.artist.dto;

import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.negongal.hummingbird.domain.artist.domain.Artist;
import com.querydsl.core.annotations.QueryProjection;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
		List<ArtistGenresDto> genresDto = artist.getGenres().stream().map(
				genre -> {
					ArtistGenresDto dto = ArtistGenresDto.builder()
						.name(genre.getName())
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
