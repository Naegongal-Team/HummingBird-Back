package com.negongal.hummingbird.domain.artist.api;

import com.negongal.hummingbird.domain.artist.application.ArtistHeartService;
import com.negongal.hummingbird.domain.artist.dto.ArtistDetailDto;
import com.negongal.hummingbird.domain.artist.dto.ArtistDto;
import com.negongal.hummingbird.domain.artist.dto.ArtistSearchDto;
import com.negongal.hummingbird.domain.artist.application.ArtistService;
import com.negongal.hummingbird.global.auth.utils.SecurityUtil;
import com.negongal.hummingbird.global.common.response.ApiResponse;
import com.negongal.hummingbird.global.common.response.ResponseUtils;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@Tag(name = "Artist API", description = "")
@RestController
@RequiredArgsConstructor
@RequestMapping("/artist")
public class ArtistApiController {

    private final ArtistService artistService;

    private final ArtistHeartService artistHeartService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse artistsList(Pageable pageable) {
        Optional<Long> currentUserId = SecurityUtil.getCurrentUserId();
        Page<ArtistDto> artistList = artistService.findArtists(currentUserId, pageable);
        HashMap<String, Page<ArtistDto>> response = new HashMap<>();
        response.put("artist_list", artistList);
        return ResponseUtils.success(response);
    }

    @GetMapping("/search/{artistName}")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse artistByNameList(@PathVariable String artistName) {
        List<ArtistSearchDto> artistSearchList = artistService.findArtistByName(artistName);
        return ResponseUtils.success(artistSearchList);
    }

    @GetMapping("/{artistId}")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse artistDetails(@PathVariable String artistId) {
        Optional<Long> currentUserId = SecurityUtil.getCurrentUserId();
        ArtistDetailDto artist = artistService.findArtist(currentUserId, artistId);
        return ResponseUtils.success(artist);
    }

    @GetMapping("/artist/heart")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse heartedArtistsList(Pageable pageable) {
        Optional<Long> currentUserId = SecurityUtil.getCurrentUserId();
        Page<ArtistDto> heartedArtistList = artistService.findLikeArtist(currentUserId,
                pageable);
        HashMap<String, Page<ArtistDto>> response = new HashMap<>();
        response.put("heartedArtist_list", heartedArtistList);
        return ResponseUtils.success(response);
    }

    @PostMapping("/{artistId}/heart")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse artistHeartAdd(@PathVariable String artistId,
                                      @RequestParam boolean isHearted) {
        Optional<Long> currentUserId = SecurityUtil.getCurrentUserId();
        if (isHearted) {
            artistHeartService.delete(currentUserId, artistId);
            return ResponseUtils.success("성공적으로 삭제되었습니다.");
        }
        artistHeartService.save(currentUserId, artistId);
        return ResponseUtils.success("성공적으로 저장되었습니다.");
    }
}
