package com.negongal.hummingbird.domain.performance.dto.response;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.negongal.hummingbird.domain.performance.domain.Performance;
import com.querydsl.core.annotations.QueryProjection;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@JsonInclude(Include.NON_EMPTY)
public class PerformanceDto {
	@JsonProperty("performance_id")
	private Long id;
	private String name;
	private String artistName;
	private String photo;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm", timezone = "Asia/Seoul")
	private LocalDateTime date;

	@QueryProjection
	public PerformanceDto(Long id, String name, String artistName, String photo) {
		this.id = id;
		this.name = name;
		this.artistName = artistName;
		this.photo = photo;
	}

	@Builder
	@QueryProjection
	public PerformanceDto(Long id, String name, String artistName, String photo, LocalDateTime date) {
		this.id = id;
		this.name = name;
		this.artistName = artistName;
		this.photo = photo;
		this.date = date;
	}

	public static PerformanceDto of(Performance p) {
		return PerformanceDto.builder()
			.id(p.getId())
			.name(p.getName())
			.artistName(p.getArtist().getName())
			.photo(p.getPhoto())
			.build();
	}
}
