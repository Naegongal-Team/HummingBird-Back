package com.negongal.hummingbird.domain.artist.api;

import com.negongal.hummingbird.domain.artist.application.SpotifyService;
import com.negongal.hummingbird.domain.artist.dto.ArtistSearchDto;
import com.negongal.hummingbird.global.common.response.ApiResponse;
import com.negongal.hummingbird.global.common.response.ResponseUtils;
import com.wrapper.spotify.exceptions.SpotifyWebApiException;
import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.apache.hc.core5.http.ParseException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/spotify")
public class SpotifyApiController {

    private final SpotifyService spotifyService;

    @GetMapping("/search/{artistName}")
    public ApiResponse<List<ArtistSearchDto>> spotifyArtistSearchList(@PathVariable String artistName)
            throws IOException, ParseException, SpotifyWebApiException {
        List<ArtistSearchDto> searchedArtists = spotifyService.searchArtists(artistName);
        return ResponseUtils.success(searchedArtists);
    }
}