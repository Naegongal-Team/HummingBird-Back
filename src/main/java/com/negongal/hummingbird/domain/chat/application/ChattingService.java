package com.negongal.hummingbird.domain.chat.application;

import com.negongal.hummingbird.domain.chat.dao.ChattingMessageRepository;
import com.negongal.hummingbird.domain.chat.dao.ChattingRoomRepository;
import com.negongal.hummingbird.domain.chat.domain.ChattingMessage;
import com.negongal.hummingbird.domain.chat.domain.ChattingRoom;
import com.negongal.hummingbird.domain.chat.dto.ChattingMessageDto;
import com.negongal.hummingbird.domain.chat.dto.ChattingRoomDto;
import com.negongal.hummingbird.domain.performance.dao.PerformanceRepository;
import com.negongal.hummingbird.domain.performance.domain.Performance;
import com.negongal.hummingbird.domain.user.dao.UserRepository;
import com.negongal.hummingbird.domain.user.domain.User;
import com.negongal.hummingbird.global.error.ErrorCode;
import com.negongal.hummingbird.global.error.exception.NotExistException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ChattingService {

    private final ChattingRoomRepository chattingRoomRepository;
    private final ChattingMessageRepository chattingMessageRepository;
    private final PerformanceRepository performanceRepository;
    private final UserRepository userRepository;

    //채팅방 생성
    @Transactional
    public void save(Long performanceId) {
        Performance performance = performanceRepository.findById(performanceId)
                .orElseThrow(() -> new NotExistException(ErrorCode.PERFORMANCE_NOT_EXIST));

        ChattingRoom chattingRoom = ChattingRoom.builder()
                .performance(performance)
                .build();

        chattingRoomRepository.save(chattingRoom);
    }

    public ChattingRoomDto findOne(Long roomId) {
        ChattingRoom chattingRoom = chattingRoomRepository.findById(roomId).orElseThrow();
        return ChattingRoomDto.of(chattingRoom);
    }

    //메세지 저장
    @Transactional
    public ChattingMessage createChat(Long chattingRoomId, ChattingMessageDto messageDto) {
        ChattingRoom chattingRoom = chattingRoomRepository.findById(chattingRoomId).orElseThrow();
        User user = userRepository.findById(messageDto.getSenderId())
                .orElseThrow(() -> new NotExistException(ErrorCode.USER_NOT_EXIST));

        ChattingMessage message = ChattingMessage.builder()
                .content(messageDto.getContent())
                .user(user)
                .chattingRoom(chattingRoom)
                .build();
        chattingMessageRepository.save(message);

        return message;
    }
}
