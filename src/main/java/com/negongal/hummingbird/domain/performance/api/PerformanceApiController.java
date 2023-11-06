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

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/performance")
public class PerformanceApiController {

    private final PerformanceService performanceService;
    private final PerformanceHeartService performanceHeartService;
    private final TicketingService ticketService;
    private final S3Uploader uploader;

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

    @GetMapping("/{performanceId}")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<?> performanceDetails(@PathVariable Long performanceId) {
        PerformanceDetailDto Performance = performanceService.findOne(performanceId);
        return ResponseUtils.success(Performance);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<?> performanceList(Pageable pageable) {
        PerformancePageDto performancePageList = performanceService.findAll(pageable);
        return ResponseUtils.success(performancePageList);
    }

    @GetMapping("/main")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<?> performanceList(@RequestParam("size") int size, @RequestParam("sort") String sort) {
        List<PerformanceDto> performanceList = performanceService.findSeveral(size, sort);
        return ResponseUtils.success("performance_list", performanceList);
    }

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

    @GetMapping("/user/heart")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<?> performanceHeartList(Pageable pageable) {
        PerformancePageDto performancePageList = performanceHeartService.findByUserHeart(pageable);
        return ResponseUtils.success(performancePageList);
    }

    @GetMapping("/artist/{artistId}")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<?> artistPerformanceList(@PathVariable String artistId,
                                                @RequestParam boolean scheduled) { // 예정된 공연
        List<PerformanceDto> performanceList = performanceService.findByArtist(artistId, scheduled);
        return ResponseUtils.success("performance_list", performanceList);
    }

    @GetMapping("/search")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<?> performanceSearch(@RequestBody PerformanceSearchRequestDto requestDto) {
        List<PerformanceDto> performanceList = performanceService.search(requestDto);
        return ResponseUtils.success("performance_list", performanceList);
    }
}
