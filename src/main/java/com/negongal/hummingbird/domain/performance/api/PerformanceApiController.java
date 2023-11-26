package com.negongal.hummingbird.domain.performance.api;

import com.negongal.hummingbird.domain.performance.dto.PerformancePageDto;
import com.negongal.hummingbird.domain.performance.dto.PerformanceSearchRequestDto;
import com.negongal.hummingbird.global.common.response.ApiResponse;
import com.negongal.hummingbird.global.common.response.ResponseUtils;
import com.negongal.hummingbird.infra.awsS3.S3Uploader;
import com.negongal.hummingbird.domain.performance.dto.PerformanceDetailDto;
import com.negongal.hummingbird.domain.performance.dto.PerformanceDto;
import com.negongal.hummingbird.domain.performance.dto.PerformanceRequestDto;
import com.negongal.hummingbird.domain.performance.application.PerformanceHeartService;
import com.negongal.hummingbird.domain.performance.application.PerformanceService;
import com.negongal.hummingbird.domain.performance.application.TicketingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.io.IOException;
import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "Performance API", description = "")
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/performance")
public class PerformanceApiController {

    private final PerformanceService performanceService;
    private final PerformanceHeartService performanceHeartService;
    private final TicketingService ticketService;
    private final S3Uploader uploader;

    @Operation(summary = "공연 등록", description = "관리자가 공연을 등록합니다.")
    @PostMapping(value = "/admin", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse performanceAdd(
            @Valid @RequestPart(value = "performance") PerformanceRequestDto requestDto,
            @RequestPart(required = false, value = "photo") MultipartFile photo) throws IOException {
        String photoUrl = (photo == null) ? null : uploader.saveFile(photo);
        Long performanceId = performanceService.save(requestDto, photoUrl);
        ticketService.save(performanceId, requestDto);
        return ResponseUtils.success();
    }

    @Operation(summary = "공연 수정", description = "관리자가 공연 정보를 수정합니다.")
    @Parameter(name = "performanceId", description = "공연 아이디 값", example = "performanceId")
    @PatchMapping(value = "/{performanceId}/admin", consumes = {MediaType.APPLICATION_JSON_VALUE,
            MediaType.MULTIPART_FORM_DATA_VALUE})
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<?> performanceModify(
            @PathVariable Long performanceId,
            @Valid @RequestPart(value = "performance") PerformanceRequestDto requestDto,
            @RequestPart(required = false, value = "photo") MultipartFile photo) throws IOException {
        String photoUrl = (photo == null) ? null : uploader.saveFile(photo);
        performanceService.update(performanceId, requestDto, photoUrl);
        ticketService.save(performanceId, requestDto);
        return ResponseUtils.success();
    }

    @Operation(summary = "공연 디테일 조회", description = "공연 디테일 정보를 조회합니다.")
    @Parameter(name = "performanceId", description = "공연 아이디 값", example = "performanceId")
    @GetMapping("/{performanceId}")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<?> performanceDetails(@PathVariable Long performanceId) {
        PerformanceDetailDto Performance = performanceService.findOne(performanceId);
        return ResponseUtils.success(Performance);
    }

    @Operation(summary = "전체 공연 리스트 조회", description = "전체 공연 리스트를 공연 날짜 순, 티켓팅 날짜 순, 인기있는 공연 순으로 조회합니다.")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<?> performanceList(Pageable pageable) {
        PerformancePageDto performancePageList = performanceService.findAll(pageable);
        return ResponseUtils.success(performancePageList);
    }

    @Operation(summary = "메인 페이지 공연 리스트 조회", description = "메인 페이지에서 공연 리스트를 공연 날짜 순, 인기있는 공연 순으로 조회합니다.")
    @GetMapping("/main")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<?> performanceList(@RequestParam("size") int size, @RequestParam("sort") String sort) {
        List<PerformanceDto> performanceList = performanceService.findSeveral(size, sort);
        return ResponseUtils.success("performance_list", performanceList);
    }

    @Operation(summary = "공연 좋아요", description = "유저가 해당 공연의 좋아요를 등록했다가 해제할 수 있습니다.")
    @Parameter(name = "performanceId", description = "공연 아이디 값", example = "performanceId")
    @Parameter(name = "check", description = "공연 좋아요 유무", example = "true")
    @PostMapping("/{performanceId}/heart")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<?> performanceHeartAdd(@PathVariable Long performanceId, @RequestParam boolean check) {
        if (check) {
            performanceHeartService.save(performanceId);
        } else {
            performanceHeartService.delete(performanceId);
        }
        return ResponseUtils.success();
    }

    @Operation(summary = "유저가 좋아요한 공연 리스트 조회", description = "유저가 좋아요한 공연 리스트 정보를 조회합니다.")
    @GetMapping("/user/heart")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<?> performanceHeartList(Pageable pageable) {
        PerformancePageDto performancePageList = performanceHeartService.findByUserHeart(pageable);
        return ResponseUtils.success(performancePageList);
    }

    @Operation(summary = "가수의 공연 리스트 조회", description = "가수의 예정된 공연과 지난 공연을 조회합니다.")
    @Parameter(name = "artistId", description = "가수 아이디 값", example = "artistId")
    @Parameter(name = "scheduled", description = "예정된 공연(true)인지 지난 공연(false)인지 유무", example = "true")
    @GetMapping("/artist/{artistId}")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<?> artistPerformanceList(@PathVariable String artistId,
                                                @RequestParam boolean scheduled) { // 예정된 공연
        List<PerformanceDto> performanceList = performanceService.findByArtist(artistId, scheduled);
        return ResponseUtils.success("performance_list", performanceList);
    }

    @Operation(summary = "공연 검색", description = "가수 이름으로 공연을 검색할 수 있습니다.")
    @GetMapping("/search")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<?> performanceSearch(@RequestBody PerformanceSearchRequestDto requestDto) {
        List<PerformanceDto> performanceList = performanceService.search(requestDto);
        return ResponseUtils.success("performance_list", performanceList);
    }
}
