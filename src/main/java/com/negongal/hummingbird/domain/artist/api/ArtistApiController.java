package com.negongal.hummingbird.domain.artist.api;

import com.google.firebase.messaging.FirebaseMessagingException;
import com.negongal.hummingbird.domain.artist.application.ArtistHeartService;
import com.negongal.hummingbird.domain.artist.dto.ArtistDetailDto;
import com.negongal.hummingbird.domain.artist.dto.ArtistDto;
import com.negongal.hummingbird.domain.artist.dto.ArtistSearchDto;
import com.negongal.hummingbird.domain.artist.application.ArtistService;
import com.negongal.hummingbird.global.common.response.ApiResponse;
import com.negongal.hummingbird.global.common.response.ResponseUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
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

    @Operation(summary = "아티스트 전체 조회", description = "등록되어 있는 아티스트 전체를 조회할 수 있습니다.")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse artistsList(Pageable pageable) {
        Page<ArtistDto> artists = artistService.findAllArtist(pageable);
        return ResponseUtils.success(artists);
    }

    @Operation(summary = "아티스트 이름 조회", description = "등록되어 있는 아티스트를 원하는 이름으로 검색할 수 있습니다.")
    @Parameter(name = "artist name", description = "아티스트의 이름", example = "Paledusk")
    @GetMapping("/search/{artistName}")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse artistByNameList(@PathVariable String artistName) {
        List<ArtistSearchDto> artistSearchList = artistService.findArtistByName(artistName);
        return ResponseUtils.success(artistSearchList);
    }

    @Operation(summary = "아티스트 단건 조회", description = "원하는 아티스트 한 명의 상세 정보를 조회할 수 있습니다.")
    @Parameter(name = "artist id", description = "아티스트의 아이디", example = "2GWuBfYdmPB91krBNQavHa")
    @GetMapping("/{artistId}")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse artistDetails(@PathVariable String artistId) {
        ArtistDetailDto artist = artistService.findArtist(artistId);
        return ResponseUtils.success(artist);
    }

    @Operation(summary = "좋아요한 아티스트 조회", description = "사용자의 좋아요한 아티스트 목록을 조회합니다.")
    @GetMapping("/heart")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse heartedArtistsList(Pageable pageable) {
        Page<ArtistDto> heartedArtistList = artistService.findLikeArtists(pageable);
        HashMap<String, Page<ArtistDto>> response = new HashMap<>();
        response.put("heartedArtist_list", heartedArtistList);
        return ResponseUtils.success(response);
    }

    @Operation(summary = "아티스트 좋아요 추가", description = "사용자가 아티스트 좋아요 혹은 취소할 수 있습니다.")
    @Parameter(name = "artist id", description = "아티스트의 아이디", example = "2GWuBfYdmPB91krBNQavHa")
    @PostMapping("/{artistId}/heart")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse artistHeartToggle(@PathVariable String artistId,
                                         @RequestParam(required = true) boolean isHearted) {
        if (isHearted) {
            artistHeartService.delete(artistId);
            return ResponseUtils.success("좋아요 삭제가 완료되었습니다.");
        }
        artistHeartService.save(artistId);
        return ResponseUtils.success("좋아요 등록이 완료되었습니다.");
    }

    @Operation(summary = "아티스트 알람 등록", description = "아티스트의 새 공연에 대한 알람 등록을 할 수 있습니다.")
    @Parameters({
            @Parameter(name = "artist id", description = "아티스트의 아이디", example = "2GWuBfYdmPB91krBNQavHa"),
            @Parameter(name = "heart validation", description = "하트의 유무", example = "false")
    })
    @PostMapping("/{artistId}/alarm")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse artistAlarmModify(@PathVariable String artistId,
                                         @RequestParam(required = true) boolean isHearted)
            throws FirebaseMessagingException {
        if (isHearted == false) {
            return ResponseUtils.error("A003", "아티스트 좋아요 값이 false입니다.");
        }
        boolean isAlarmed = artistHeartService.modifyArtistAlarm(artistId);

        if (isAlarmed) {
            return ResponseUtils.success("알람 취소가 완료되었습니다.");
        }
        return ResponseUtils.success("알람 등록이 완료되었습니다");
    }
}
