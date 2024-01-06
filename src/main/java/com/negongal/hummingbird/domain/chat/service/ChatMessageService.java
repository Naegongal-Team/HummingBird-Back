package com.negongal.hummingbird.domain.chat.service;

import static com.negongal.hummingbird.global.error.ErrorCode.CHAT_ROOM_NOT_EXIST;
import static com.negongal.hummingbird.global.error.ErrorCode.USER_NOT_EXIST;

import com.negongal.hummingbird.domain.chat.dao.ChatMessageRepository;
import com.negongal.hummingbird.domain.chat.dao.ChatRoomRepository;
import com.negongal.hummingbird.domain.chat.domain.ChatMessage;
import com.negongal.hummingbird.domain.chat.domain.ChatRoom;
import com.negongal.hummingbird.domain.chat.dto.ChatMessageDto;
import com.negongal.hummingbird.domain.chat.dto.ChatMessagePageDto;
import com.negongal.hummingbird.domain.chat.dto.ChatMessageResponseDto;
import com.negongal.hummingbird.domain.chat.view.ViewChatMessage;
import com.negongal.hummingbird.domain.performance.dto.PerformancePageDto;
import com.negongal.hummingbird.domain.user.dao.UserRepository;
import com.negongal.hummingbird.domain.user.domain.User;
import com.negongal.hummingbird.global.auth.utils.SecurityUtil;
import com.negongal.hummingbird.global.error.exception.NotExistException;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatMessageService {

    private final ChatMessageRepository chatMessageRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final UserRepository userRepository;

    public void saveMessage(ChatMessageDto messageDto) {
        ChatRoom chatRoom = chatRoomRepository.findByRoomId(messageDto.getRoomId())
                .orElseThrow(() -> new NotExistException(CHAT_ROOM_NOT_EXIST));

        User user = userRepository.findByNickname(messageDto.getNickname())
                .orElseThrow(() -> new NotExistException(USER_NOT_EXIST));

        ChatMessage chatMessage = ChatMessage.builder()
                .chatRoom(chatRoom)
                .user(user)
                .type(messageDto.getType())
                .content(messageDto.getContent())
                .sendTime(messageDto.getSendTime())
                .build();
        chatMessageRepository.save(chatMessage);
    }

    public ChatMessagePageDto loadMessage(String roomId, Pageable pageable) {
        ChatRoom chatRoom = chatRoomRepository.findByRoomId(roomId)
                .orElseThrow(() -> new NotExistException(CHAT_ROOM_NOT_EXIST));

        Page<ChatMessageResponseDto> dtoPage = chatMessageRepository.findAllCustom(chatRoom.getId(), pageable);

        return ChatMessagePageDto.builder()
                .chatMessageDto(dtoPage.getContent())
                .totalPages(dtoPage.getTotalPages())
                .totalElements(dtoPage.getTotalElements())
                .isLast(dtoPage.isLast())
                .currPage(dtoPage.getPageable().getPageNumber())
                .build();
    }
}
