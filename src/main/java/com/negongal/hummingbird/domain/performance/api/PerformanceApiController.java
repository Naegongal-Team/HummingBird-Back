package com.negongal.hummingbird.domain.performance.api;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
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

import com.negongal.hummingbird.domain.chat.service.ChatRoomService;
import com.negongal.hummingbird.domain.notification.application.NotificationService;
import com.negongal.hummingbird.domain.performance.application.PerformanceHeartService;
import com.negongal.hummingbird.domain.performance.application.PerformanceService;
import com.negongal.hummingbird.domain.performance.application.TicketingService;
import com.negongal.hummingbird.domain.performance.dto.request.PerformanceRequestDto;
import com.negongal.hummingbird.domain.performance.dto.request.PerformanceSearchRequestDto;
import com.negongal.hummingbird.domain.performance.dto.response.PerformanceDetailDto;
import com.negongal.hummingbird.domain.performance.dto.response.PerformanceDto;
import com.negongal.hummingbird.domain.performance.dto.response.PerformancePageDto;
import com.negongal.hummingbird.global.auth.model.CustomUserDetail;
import com.negongal.hummingbird.global.common.response.ApiResponse;
import com.negongal.hummingbird.global.common.response.ResponseUtils;
import com.negongal.hummingbird.infra.awsS3.S3Uploader;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Tag(name = "Performance API", description = "")
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/performance")
public class PerformanceApiController {

	private final PerformanceService performanceService;
	private final PerformanceHeartService performanceHeartService;
	private final TicketingService ticketService;
	private final ChatRoomService chatRoomService;
	private final S3Uploader uploader;
	private final NotificationService notificationService;

	@Operation(summary = "공연 등록", description = "관리자가 공연을 등록합니다.",
		security = {@SecurityRequirement(name = "access-token")})
	@PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
	@PreAuthorize("hasRole('ADMIN')")
	@ResponseStatus(HttpStatus.CREATED)
	public ApiResponse<Void> performanceAdd(
		@Valid @RequestPart(value = "performance") PerformanceRequestDto requestDto,
		@RequestPart(required = false, value = "photo") MultipartFile photo) throws IOException {
		String photoUrl = (photo == null) ? null : uploader.saveFile(photo);
		Long performanceId = performanceService.savePerformance(requestDto, photoUrl);
		ticketService.saveTicketing(performanceId, requestDto);
		chatRoomService.createChatRoom(performanceId); /** 채팅방 개설 **/
		notificationService.pushPerformRegisterNotification(requestDto.getArtistName());
		return ResponseUtils.success();
	}

	@Operation(summary = "공연 수정", description = "관리자가 공연 정보를 수정합니다.",
		security = {@SecurityRequirement(name = "access-token")})
	@Parameter(name = "performanceId", description = "공연 아이디 값", example = "performanceId")
	@PatchMapping(value = "/{performanceId}", consumes = {MediaType.APPLICATION_JSON_VALUE,
		MediaType.MULTIPART_FORM_DATA_VALUE})
	@PreAuthorize("hasRole('ADMIN')")
	public ApiResponse<Void> performanceModify(
		@PathVariable Long performanceId,
		@Valid @RequestPart(value = "performance") PerformanceRequestDto requestDto,
		@RequestPart(required = false, value = "photo") MultipartFile photo) throws IOException {
		String photoUrl = (photo == null) ? null : uploader.saveFile(photo);
		performanceService.updatePerformance(performanceId, requestDto, photoUrl);
		ticketService.saveTicketing(performanceId, requestDto);
		return ResponseUtils.success();
	}

	@Operation(summary = "공연 삭제", description = "관리자가 공연을 삭제합니다.",
		security = {@SecurityRequirement(name = "access-token")})
	@Parameter(name = "performanceId", description = "공연 아이디 값", example = "performanceId")
	@DeleteMapping("/{performanceId}/admin")
	@PreAuthorize("hasRole('ADMIN')")
	public ApiResponse<Void> performanceRemove(@PathVariable Long performanceId) {
		performanceService.deletePerformance(performanceId);
		return ResponseUtils.success();
	}

	@Operation(summary = "공연 디테일 조회", description = "공연 디테일 정보를 조회합니다.")
	@Parameter(name = "performanceId", description = "공연 아이디 값", example = "performanceId")
	@GetMapping("/{performanceId}")
	public ApiResponse<PerformanceDetailDto> performanceDetails(@PathVariable Long performanceId) {
		PerformanceDetailDto Performance = performanceService.findPerformance(performanceId);
		return ResponseUtils.success(Performance);
	}

	@Operation(summary = "전체 공연 리스트 조회", description = "전체 공연 리스트를 공연 날짜 순, 티켓팅 날짜 순, 인기있는 공연 순으로 조회합니다.")
	@GetMapping
	public ApiResponse<PerformancePageDto> performanceList(Pageable pageable) {
		PerformancePageDto performancePageList = performanceService.findAllPerformance(pageable);
		return ResponseUtils.success(performancePageList);
	}

	@Operation(summary = "메인 페이지 공연 리스트 조회", description = "메인 페이지에서 공연 리스트를 공연 날짜 순, 인기있는 공연 순으로 조회합니다.")
	@GetMapping("/main")
	public ApiResponse<Map<String, List<PerformanceDto>>> performanceMainList(@RequestParam("size") int size,
		@RequestParam("sort") String sort) {
		List<PerformanceDto> performanceList = performanceService.findMainPerformances(size, sort);
		return ResponseUtils.success("performance_list", performanceList);
	}

	@Operation(summary = "공연 좋아요", description = "유저가 해당 공연의 좋아요를 등록했다가 해제할 수 있습니다.",
		security = {@SecurityRequirement(name = "access-token")})
	@Parameter(name = "performanceId", description = "공연 아이디 값", example = "performanceId")
	@Parameter(name = "isHearted", description = "현재 공연 좋아요 유무", example = "true")
	@PostMapping("/{performanceId}/heart")
	@PreAuthorize("hasRole('USER')")
	@ResponseStatus(HttpStatus.CREATED)
	public ApiResponse<String> performanceHeartAdd(@PathVariable Long performanceId, @RequestParam boolean isHearted,
		@AuthenticationPrincipal CustomUserDetail userDetail) {
		if (isHearted) {
			performanceHeartService.deletePerformanceHeart(performanceId, userDetail.getUserId());
			return ResponseUtils.success("좋아요 삭제가 완료되었습니다.");
		}
		performanceHeartService.savePerformanceHeart(performanceId, userDetail.getUserId());
		return ResponseUtils.success("좋아요 등록이 완료되었습니다.");
	}

	@Operation(summary = "유저가 좋아요한 공연 리스트 조회", description = "유저가 좋아요한 공연 리스트 정보를 조회합니다.",
		security = {@SecurityRequirement(name = "access-token")})
	@GetMapping("/user/heart")
	@PreAuthorize("hasRole('USER')")
	public ApiResponse<PerformancePageDto> performanceHeartList(Pageable pageable,
		@AuthenticationPrincipal CustomUserDetail userDetail) {
		PerformancePageDto performancePageList = performanceHeartService.findPerformancesByUserHeart(
			userDetail.getUserId(), pageable);
		return ResponseUtils.success(performancePageList);
	}

	@Operation(summary = "가수의 공연 리스트 조회", description = "가수의 예정된 공연과 지난 공연을 조회합니다.")
	@Parameter(name = "artistId", description = "가수 아이디 값", example = "artistId")
	@Parameter(name = "scheduled", description = "예정된 공연(true)인지 지난 공연(false)인지 유무", example = "true")
	@GetMapping("/artist/{artistId}")
	public ApiResponse<Map<String, List<PerformanceDto>>> artistPerformanceList(@PathVariable String artistId,
		@RequestParam boolean scheduled) { // 예정된 공연
		List<PerformanceDto> performanceList = performanceService.findPerformancesByArtist(artistId, scheduled);
		return ResponseUtils.success("performance_list", performanceList);
	}

	@Operation(summary = "공연 검색", description = "가수 이름으로 공연을 검색할 수 있습니다.")
	@GetMapping("/search")
	public ApiResponse<PerformancePageDto> performanceSearch(@RequestBody PerformanceSearchRequestDto requestDto,
		Pageable pageable) {
		PerformancePageDto performancePageList = performanceService.searchPerformances(requestDto, pageable);
		return ResponseUtils.success(performancePageList);
	}
}
