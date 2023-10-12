package com.negongal.hummingbird.service;

import com.negongal.hummingbird.api.dto.PerformanceRequestDto;
import com.negongal.hummingbird.domain.Performance;
import com.negongal.hummingbird.domain.Ticketing;
import com.negongal.hummingbird.domain.TicketType;
import com.negongal.hummingbird.repository.PerformanceRepository;
import com.negongal.hummingbird.repository.TicketingRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class TicketingService {

    private final TicketingRepository ticketingRepository;
    private final PerformanceRepository performanceRepository;

    @Transactional
    public void save(Long performanceId, PerformanceRequestDto requestDto){
        Performance performance = performanceRepository.findById(performanceId)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 공연입니다."));

        List<Ticketing> ticketingList = new ArrayList<>();
        if(requestDto.getRegularTicketing() != null) {
            List<Ticketing> rTicketing = requestDto.getRegularTicketing().stream()
                    .map(t -> t.toEntity(performance, TicketType.REGULAR))
                    .collect(Collectors.toList());
            ticketingList.addAll(rTicketing);
        }

        if(requestDto.getEarlybirdTicketing() != null) {
            List<Ticketing> eTicketing = requestDto.getEarlybirdTicketing().stream()
                    .map(t -> t.toEntity(performance, TicketType.EARLY_BIRD))
                    .collect(Collectors.toList());
            ticketingList.addAll(eTicketing);
        }

        ticketingRepository.saveAll(ticketingList);
    }
}
