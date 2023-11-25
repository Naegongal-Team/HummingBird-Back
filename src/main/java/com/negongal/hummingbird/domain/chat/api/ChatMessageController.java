package com.negongal.hummingbird.domain.chat.api;

import com.negongal.hummingbird.domain.chat.domain.MessageType;
import com.negongal.hummingbird.domain.chat.dto.ChatMessageDto;
import com.negongal.hummingbird.domain.chat.service.ChatMessageService;
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
    private final ChatMessageService chatMessageService;

    @MessageMapping("/chat/message")
    public void message(ChatMessageDto message) {
        if(message.getType().equals(MessageType.ENTER)) {
            chatRoomService.enterChatRoom(message.getRoomId());
        }
        else {
            chatMessageService.saveMessage(message);
            redisPublisher.publish(chatRoomService.getTopic(message.getRoomId()), message);
        }
    }

}
