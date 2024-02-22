package com.negongal.hummingbird.domain.artist.api;

import java.io.IOException;
import java.util.List;

import org.apache.hc.core5.http.ParseException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.negongal.hummingbird.domain.artist.application.SpotifyService;
import com.negongal.hummingbird.domain.artist.dto.ArtistSearchDto;
import com.negongal.hummingbird.global.common.response.ApiResponse;
import com.negongal.hummingbird.global.common.response.ResponseUtils;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;

@Tag(name = "Spotify API", description = "관리자가 스포티파이 API를 사용할 수 있게 도와주는 api입니다.")
@RestController
@RequiredArgsConstructor
@RequestMapping("/spotify")
public class SpotifyApiController {

	private final SpotifyService spotifyService;

	@Operation(summary = "스포티파이 등록 가수 검색", description = "관리자가 스포티파이와 연동되어 있는 아티스트를 검색합니다.")
	@Parameter(name = "artist name", description = "아티스트의 이름", example = "paledusk")
	@GetMapping("/search/{artistName}")
	public ApiResponse<List<ArtistSearchDto>> spotifyArtistSearchList(@PathVariable String artistName)
		throws IOException, ParseException, SpotifyWebApiException {
		List<ArtistSearchDto> searchedArtists;
		searchedArtists = spotifyService.searchArtists(artistName);
		return ResponseUtils.success(searchedArtists);
	}

	@Operation(summary = "스포티파이 등록 가수 저장", description = "관리자가 스포티파이와 연동되어 있는 아티스트를 서버 db로 저장합니다.")
	@Parameter(name = "artist name", description = "아티스트의 이름", example = "paledusk")
	@PostMapping("/{artistName}")
	public ApiResponse spotifyArtistSave(@PathVariable String artistName)
		throws IOException, ParseException, SpotifyWebApiException {
		spotifyService.saveArtist(artistName);
		return ResponseUtils.success("성공적으로 저장되었습니다");
	}
}
