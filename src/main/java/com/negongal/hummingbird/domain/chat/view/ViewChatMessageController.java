package com.negongal.hummingbird.domain.chat.view;

import com.negongal.hummingbird.domain.chat.dao.ChatRoomRepository;
import com.negongal.hummingbird.domain.chat.domain.MessageType;
import com.negongal.hummingbird.domain.chat.dto.ChatMessageDto;
import com.negongal.hummingbird.domain.chat.service.ChatMessageService;
import com.negongal.hummingbird.domain.chat.service.ChatRoomService;
import com.negongal.hummingbird.global.common.pubsub.RedisPublisher;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Slf4j
@Controller
@RequiredArgsConstructor
public class ViewChatMessageController {
    private final RedisPublisher redisPublisher;
    private final ChatRoomService chatRoomService;
    private final ChatMessageService chatMessageService;
    private final ViewChatService viewChatService;

    @MessageMapping("/view/chat/message")
    public void viewMessage(ChatMessageDto message) {
        if(message.getType().equals(MessageType.ENTER)) {
            message.setEnterMessage();
            chatRoomService.enterChatRoom(message.getRoomId());
        }
        chatMessageService.saveMessage(message);
        redisPublisher.publish(chatRoomService.getTopic(message.getRoomId()), message);
    }

    /**
     * 채팅방 메시지 내역 조회
     */
    @GetMapping("/view/chat/{roomId}/message")
    public ResponseEntity<List<ViewChatMessage>> findAllMessage(@PathVariable String roomId) {
        return ResponseEntity.ok(viewChatService.loadMessage(roomId));
    }
}
