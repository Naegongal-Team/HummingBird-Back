package com.negongal.hummingbird.domain.performance.application;

import static com.negongal.hummingbird.global.error.ErrorCode.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.negongal.hummingbird.domain.performance.dao.PerformanceRepository;
import com.negongal.hummingbird.domain.performance.dao.TicketingRepository;
import com.negongal.hummingbird.domain.performance.domain.Performance;
import com.negongal.hummingbird.domain.performance.domain.TicketType;
import com.negongal.hummingbird.domain.performance.domain.Ticketing;
import com.negongal.hummingbird.domain.performance.dto.request.PerformanceRequestDto;
import com.negongal.hummingbird.domain.performance.dto.request.TicketingRequestDto;
import com.negongal.hummingbird.global.error.exception.NotExistException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class TicketingService {

	private final TicketingRepository ticketingRepository;
	private final PerformanceRepository performanceRepository;

	@Transactional
	public void saveTicketing(Long performanceId, PerformanceRequestDto requestDto) {
		Performance performance = performanceRepository.findById(performanceId)
			.orElseThrow(() -> new NotExistException(PERFORMANCE_NOT_EXIST));

		List<Ticketing> ticketingList = new ArrayList<>();
		if (requestDto.getRegularTicketing() != null) {
			ticketingList.addAll(convertToEntity(performance, TicketType.REGULAR, requestDto.getRegularTicketing()));
		}

		if (requestDto.getEarlybirdTicketing() != null) {
			ticketingList.addAll(
				convertToEntity(performance, TicketType.EARLY_BIRD, requestDto.getEarlybirdTicketing()));
		}

		ticketingRepository.saveAll(ticketingList);
	}

	public List<Ticketing> convertToEntity(Performance p, TicketType type, List<TicketingRequestDto> request) {
		return request.stream()
			.map(t -> t.toEntity(p, type))
			.collect(Collectors.toList());
	}
}
