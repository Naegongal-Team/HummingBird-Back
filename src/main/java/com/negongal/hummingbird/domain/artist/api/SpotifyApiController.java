package com.negongal.hummingbird.domain.artist.api;

import com.negongal.hummingbird.domain.artist.application.SpotifyService;
import com.negongal.hummingbird.domain.artist.dto.ArtistSearchDto;
import com.negongal.hummingbird.global.common.response.ApiResponse;
import com.negongal.hummingbird.global.common.response.ResponseUtils;

import com.negongal.hummingbird.global.error.ErrorCode;
import com.negongal.hummingbird.global.error.exception.InvalidException;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.apache.hc.core5.http.ParseException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;

@Tag(name = "Spotify API", description = "")
@RestController
@RequiredArgsConstructor
@RequestMapping("/spotify")
public class SpotifyApiController {

    private final SpotifyService spotifyService;

    @GetMapping("/search/{artistName}")
    public ApiResponse<List<ArtistSearchDto>> spotifyArtistSearchList(@PathVariable String artistName) {
        List<ArtistSearchDto> searchedArtists = new ArrayList<>();
        try {
            searchedArtists = spotifyService.searchArtists(artistName);
        } catch (IOException | ParseException | SpotifyWebApiException e) {
            new InvalidException(ErrorCode.SPOTIFY_CAN_NOT_WORK);
        }
        return ResponseUtils.success(searchedArtists);
    }

    @PostMapping("/{artistName}")
    public ApiResponse spotifyArtistSave(@PathVariable String artistName) {
        try {
            spotifyService.saveArtist(artistName);
        } catch (IOException | ParseException | SpotifyWebApiException e) {
            new InvalidException(ErrorCode.SPOTIFY_CAN_NOT_WORK);
        }
        return ResponseUtils.success("성공적으로 저장되었습니다");
    }
}
