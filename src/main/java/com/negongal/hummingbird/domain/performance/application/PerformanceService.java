package com.negongal.hummingbird.domain.performance.application;

import static com.negongal.hummingbird.global.error.ErrorCode.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.negongal.hummingbird.domain.artist.dao.ArtistRepository;
import com.negongal.hummingbird.domain.artist.domain.Artist;
import com.negongal.hummingbird.domain.notification.application.NotificationService;
import com.negongal.hummingbird.domain.notification.dao.NotificationRepository;
import com.negongal.hummingbird.domain.performance.dao.PerformanceDateRepository;
import com.negongal.hummingbird.domain.performance.dao.PerformanceHeartRepository;
import com.negongal.hummingbird.domain.performance.dao.PerformanceRepository;
import com.negongal.hummingbird.domain.performance.domain.Performance;
import com.negongal.hummingbird.domain.performance.domain.PerformanceDate;
import com.negongal.hummingbird.domain.performance.dto.request.PerformanceRequestDto;
import com.negongal.hummingbird.domain.performance.dto.request.PerformanceSearchRequestDto;
import com.negongal.hummingbird.domain.performance.dto.response.PerformanceDetailDto;
import com.negongal.hummingbird.domain.performance.dto.response.PerformanceDto;
import com.negongal.hummingbird.domain.performance.dto.response.PerformancePageDto;
import com.negongal.hummingbird.domain.user.dao.UserRepository;
import com.negongal.hummingbird.domain.user.domain.User;
import com.negongal.hummingbird.global.auth.utils.SecurityUtil;
import com.negongal.hummingbird.global.error.exception.NotExistException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PerformanceService {

	private final NotificationService notificationService;
	private final PerformanceRepository performanceRepository;
	private final PerformanceHeartRepository performanceHeartRepository;
	private final PerformanceDateRepository dateRepository;
	private final ArtistRepository artistRepository;
	private final UserRepository userRepository;
	private final NotificationRepository notificationRepository;

	@Transactional
	public Long savePerformance(PerformanceRequestDto requestDto, String photo) {
		Artist artist = artistRepository.findByName(requestDto.getArtistName())
			.orElseThrow(() -> new NotExistException(ARTIST_NOT_EXIST));

		Performance savePerformance = performanceRepository.save(requestDto.toEntity(artist, photo));

		List<PerformanceDate> dateList = createPerformanceDate(requestDto.getDates(), savePerformance);
		dateRepository.saveAll(dateList);

		String topicName = artist.getId();
		notificationService.pushPerformRegisterNotification(topicName);

		return savePerformance.getId();
	}

	@Transactional
	public void updatePerformance(Long performanceId, PerformanceRequestDto request, String photo) {
		Performance findPerformance = performanceRepository.findById(performanceId)
			.orElseThrow(() -> new NotExistException(PERFORMANCE_NOT_EXIST));

		Artist artist = artistRepository.findByName(request.getArtistName())
			.orElseThrow(() -> new NotExistException(ARTIST_NOT_EXIST));

		findPerformance.update(request.getName(), artist, request.getLocation(), request.getRuntime(),
			request.getDescription(), photo);
		dateRepository.saveAll(createPerformanceDate(request.getDates(), findPerformance));
	}

	@Transactional
	public void deletePerformance(Long performanceId) {
		Performance findPerformance = performanceRepository.findById(performanceId)
			.orElseThrow(() -> new NotExistException(PERFORMANCE_NOT_EXIST));
		performanceRepository.delete(findPerformance);
	}

	public List<PerformanceDate> createPerformanceDate(List<LocalDateTime> dateList, Performance performance) {
		return dateList.stream().map(d -> PerformanceDate.builder()
				.performance(performance)
				.startDate(d)
				.build())
			.collect(Collectors.toList());
	}

	public PerformancePageDto findAllPerformance(Pageable pageable) {
		Page<PerformanceDto> dtoPage = performanceRepository.findAllCustom(pageable);
		return PerformancePageDto.builder()
			.performanceDtos(dtoPage.getContent())
			.totalPages(dtoPage.getTotalPages())
			.totalElements(dtoPage.getTotalElements())
			.isLast(dtoPage.isLast())
			.currPage(dtoPage.getPageable().getPageNumber())
			.build();
	}

	public List<PerformanceDto> findMainPerformances(int size, String sort) {
		return performanceRepository.findSeveral(size, sort);
	}

	public PerformanceDetailDto findPerformance(Long performanceId) {
		Performance performance = performanceRepository.findById(performanceId)
			.orElseThrow(() -> new NotExistException(PERFORMANCE_NOT_EXIST));

		boolean heartPressed = false;
		boolean isAlarmed = false;
		if (SecurityUtil.getCurrentUserId().isPresent()) {
			Long userId = SecurityUtil.getCurrentUserId().get();
			User user = userRepository.findById(userId).orElseThrow(() -> new NotExistException(USER_NOT_EXIST));
			heartPressed = performanceHeartRepository.findByUserAndPerformance(user, performance).isPresent();
			if (heartPressed) {
				isAlarmed = notificationRepository.findByUserAndPerformance(user, performance).isPresent();
			}
		}

		return PerformanceDetailDto.of(performance, heartPressed, isAlarmed);
	}

	public List<PerformanceDto> findPerformancesByArtist(String artistId, boolean scheduled) {
		return performanceRepository.findByArtist(artistId, scheduled);
	}

	public PerformancePageDto searchPerformances(PerformanceSearchRequestDto requestDto, Pageable pageable) {
		Page<PerformanceDto> dtoPage = performanceRepository.search(requestDto, pageable);

		return PerformancePageDto.builder()
			.performanceDtos(dtoPage.getContent())
			.totalPages(dtoPage.getTotalPages())
			.totalElements(dtoPage.getTotalElements())
			.isLast(dtoPage.isLast())
			.currPage(dtoPage.getPageable().getPageNumber())
			.build();
	}
}
