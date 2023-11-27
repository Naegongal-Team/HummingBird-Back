package com.negongal.hummingbird.domain.chat.service;

import static com.negongal.hummingbird.global.error.ErrorCode.CHAT_ROOM_NOT_EXIST;
import static com.negongal.hummingbird.global.error.ErrorCode.USER_NOT_EXIST;

import com.negongal.hummingbird.domain.chat.dao.ChatMessageRepository;
import com.negongal.hummingbird.domain.chat.dao.ChatRoomRepository;
import com.negongal.hummingbird.domain.chat.domain.ChatMessage;
import com.negongal.hummingbird.domain.chat.domain.ChatRoom;
import com.negongal.hummingbird.domain.chat.dto.ChatMessageDto;
import com.negongal.hummingbird.domain.chat.dto.ChatMessageResponseDto;
import com.negongal.hummingbird.domain.chat.view.ViewChatMessage;
import com.negongal.hummingbird.domain.user.dao.UserRepository;
import com.negongal.hummingbird.domain.user.domain.User;
import com.negongal.hummingbird.global.auth.utils.SecurityUtil;
import com.negongal.hummingbird.global.error.exception.NotExistException;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
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

        /** Redis에 저장할 것인지 고려해보기 **/
    }

    public List<ChatMessageResponseDto> loadMessage(String roomId) {
        ChatRoom chatRoom = chatRoomRepository.findByRoomId(roomId)
                .orElseThrow(() -> new NotExistException(CHAT_ROOM_NOT_EXIST));
        List<ChatMessage> chatMessageList = chatMessageRepository.findByIdOrderBySendTimeAsc(chatRoom.getId());

        if(SecurityUtil.getCurrentUserId().isPresent()) {
            Long loginUserId = SecurityUtil.getCurrentUserId().get();
            return chatMessageList.stream().map(s -> ChatMessageResponseDto.of(s, loginUserId))
                    .collect(Collectors.toList());
        }

        return chatMessageList.stream().map(s -> ChatMessageResponseDto.of(s, null))
                .collect(Collectors.toList());
    }
}
