package com.negongal.hummingbird.service;

import com.negongal.hummingbird.api.dto.PerformanceDto;
import com.negongal.hummingbird.api.dto.PerformanceRequestDto;
import com.negongal.hummingbird.api.dto.TicketingDto;
import com.negongal.hummingbird.api.dto.TicketingRequestDto;
import com.negongal.hummingbird.domain.Performance;
import com.negongal.hummingbird.domain.Ticketing;
import com.negongal.hummingbird.domain.Type;
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
    public Long save(Long performanceId, PerformanceRequestDto requestDto){
        Performance performance = performanceRepository.findById(performanceId)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 공연입니다."));

        List<Ticketing> ticketingList = new ArrayList<>();
        if(requestDto.getRegularTicketing() != null) {
            List<Ticketing> ticketing = requestDto.getRegularTicketing()
                    .stream()
                    .map(t -> Ticketing.builder()
                            .performance(performance)
                            .type(Type.REGULAR)
                            .date(t.getDate())
                            .platform(t.getPlatform())
                            .link(t.getLink())
                            .description("")
                            .build())
                    .collect(Collectors.toList());
            ticketingList.addAll(ticketing);
        }

        if(requestDto.getEarlybirdTicketing() != null) {
            List<Ticketing> ticketing = requestDto.getEarlybirdTicketing()
                    .stream()
                    .map(t -> Ticketing.builder()
                            .performance(performance)
                            .type(Type.EARLY_BIRD)
                            .date(t.getDate())
                            .platform(t.getPlatform())
                            .link(t.getLink())
                            .description(t.getDescription())
                            .build())
                    .collect(Collectors.toList());

            ticketingList.addAll(ticketing);
        }

        ticketingRepository.saveAll(ticketingList);
        return null;
    }
}
