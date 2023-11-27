package com.negongal.hummingbird;

import com.negongal.hummingbird.domain.chat.service.ChatRoomService;
import com.negongal.hummingbird.domain.performance.dao.PerformanceRepository;
import com.negongal.hummingbird.domain.performance.domain.Performance;
import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TestApplicationRunner implements ApplicationRunner {

    private final PerformanceRepository performanceRepository;
    private final ChatRoomService chatRoomService;

    /**
     * 애플리케이션 실행 시, 자동 실행
     * 테스트 데이터를 위한 모든 채팅방 자동 생성
     */
    @Override
    public void run(ApplicationArguments args) throws Exception {
        List<Performance> all = performanceRepository.findAll();
        for(Performance p : all) {
            chatRoomService.createChatRoom(p.getId());
        }
    }
}
