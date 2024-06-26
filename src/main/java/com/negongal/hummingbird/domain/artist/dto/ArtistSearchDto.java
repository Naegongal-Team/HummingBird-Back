package com.negongal.hummingbird.domain.artist.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ArtistSearchDto {
	private String id;
	private String name;

	@Builder
	public ArtistSearchDto(String id, String name) {
		this.id = id;
		this.name = name;
	}
}
