package com.negongal.hummingbird.domain.artist.api;

import com.google.firebase.ErrorCode;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.negongal.hummingbird.domain.artist.application.ArtistHeartService;
import com.negongal.hummingbird.domain.artist.dto.ArtistDetailDto;
import com.negongal.hummingbird.domain.artist.dto.ArtistDto;
import com.negongal.hummingbird.domain.artist.dto.ArtistSearchDto;
import com.negongal.hummingbird.domain.artist.application.ArtistService;
import com.negongal.hummingbird.global.common.response.ApiResponse;
import com.negongal.hummingbird.global.common.response.ResponseUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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

    @Operation(summary = "아티스트 전체 조회", description = "인기 순으로 정렬 가능")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse artistsList(Pageable pageable) {
        Page<ArtistDto> artistList = artistService.findAllArtist(pageable);
        return ResponseUtils.success(artistList);
    }

    @Operation(summary = "아티스트 이름 조회", description = "")
    @GetMapping("/search/{artistName}")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse artistByNameList(@PathVariable String artistName) {
        List<ArtistSearchDto> artistSearchList = artistService.findArtistByName(artistName);
        return ResponseUtils.success(artistSearchList);
    }

    @Operation(summary = "아티스트 단건 조회", description = "아티스트의 상세 내용과 좋아요 유무 boolean 값 전달")
    @GetMapping("/{artistId}")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse artistDetails(@PathVariable String artistId) {
        ArtistDetailDto artist = artistService.findArtist(artistId);
        return ResponseUtils.success(artist);
    }

    @Operation(summary = "좋아요한 아티스트 조회", description = "아티스트의 전체 조회는 하지 않고 좋아요한 아티스트만 전달")
    @GetMapping("/heart")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse heartedArtistsList(Pageable pageable) {
        Page<ArtistDto> heartedArtistList = artistService.findLikeArtists(pageable);
        HashMap<String, Page<ArtistDto>> response = new HashMap<>();
        response.put("heartedArtist_list", heartedArtistList);
        return ResponseUtils.success(response);
    }

    @Operation(summary = "아티스트 좋아요 추가", description = "boolean값으로 isHearted를 필히 넘겨야 한다.")
    @PostMapping("/{artistId}/heart")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse artistHeartAdd(@PathVariable String artistId,
                                      @RequestParam(required = true) boolean isHearted)
            throws FirebaseMessagingException {
        if (isHearted) {
            artistHeartService.delete(artistId);
            return ResponseUtils.success("좋아요 삭제가 완료되었습니다.");
        }
        artistHeartService.save(artistId);
        return ResponseUtils.success("좋아요 등록이 완료되었습니다.");
    }
    @Operation(summary = "아티스트 알람 등록", description = "boolean값으로 isHearted를 필히 넘겨야 한다.")
    @PostMapping("/{artistId}/alarm")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse artistAlarmModify(@PathVariable String artistId,
                                         @RequestParam(required = true) boolean isHearted) {
        if (isHearted == false) {
            return ResponseUtils.error("A003", "아티스트 좋아요 값이 false입니다.");
        }
        boolean isAlarmed = artistHeartService.modifyAlarm(artistId);

        if (isAlarmed) {
            return ResponseUtils.success("알람 취소가 완료되었습니다.");
        }
        return ResponseUtils.success("알람 등록이 완료되었습니다");
    }
}
