package com.negongal.hummingbird.domain.chat.api;

import com.negongal.hummingbird.domain.chat.application.ChattingService;
import com.negongal.hummingbird.domain.chat.domain.ChattingMessage;
import com.negongal.hummingbird.domain.chat.dto.ChattingMessageDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@RequiredArgsConstructor
@Controller
@Slf4j
public class ChattingApiController {

    private final SimpMessagingTemplate simpMessagingTemplate;
    private final ChattingService chattingService;

//    @MessageMapping("/{chattingRoomId}/message")
//    public void chat(@DestinationVariable Long chattingRoomId, ChattingMessageDto messageDto) {
//        simpMessagingTemplate.convertAndSend("/sub/chatting/" + chattingRoomId, messageDto.getContent());
//    }

    @MessageMapping("/{chattingRoomId}/message")
    @SendTo("/chatting/{chattingRoomId}")
    public ChattingMessageDto chat(@DestinationVariable Long chattingRoomId, ChattingMessageDto messageDto) {
        ChattingMessage chattingMessage = chattingService.createChat(chattingRoomId, messageDto);

        return ChattingMessageDto.builder()
                .roomId(chattingMessage.getChattingRoom().getId())
                .content(chattingMessage.getContent())
                .senderId(chattingMessage.getUser().getUserId())
                .build();
    }
}
