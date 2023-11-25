package com.negongal.hummingbird.domain.chat.api;

import com.negongal.hummingbird.domain.chat.domain.MessageType;
import com.negongal.hummingbird.domain.chat.dto.ChatMessageDto;
import com.negongal.hummingbird.domain.chat.service.ChatRoomService;
import com.negongal.hummingbird.global.common.pubsub.RedisPublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

@Slf4j
@Controller
@RequiredArgsConstructor
public class ChatMessageController {

    private final RedisPublisher redisPublisher;
    private final ChatRoomService chatRoomService;

    @MessageMapping("/chat/message")
    public void message(ChatMessageDto message) {
//        log.info("ChatMessageController :: {}", message.toString());
        if(message.getType().equals(MessageType.ENTER)) {
            message.setEnterMessage();
            chatRoomService.enterChatRoom(message.getRoomId());
        }
        redisPublisher.publish(chatRoomService.getTopic(message.getRoomId()), message);
    }

}
