package com.negongal.hummingbird.domain.chat.api;

import com.negongal.hummingbird.domain.chat.dto.ChatMessageResponseDto;
import com.negongal.hummingbird.domain.chat.service.ChatMessageService;
import com.negongal.hummingbird.global.common.response.ApiResponse;
import com.negongal.hummingbird.global.common.response.ResponseUtils;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/chat/room")
public class ChatRoomController {

    private final ChatMessageService chatMessageService;

    /**
     * 채팅방 메시지 내역 조회
     */
    @GetMapping("/{roomId}/message")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<?> findAllMessage(@PathVariable String roomId) {
        List<ChatMessageResponseDto> chatMessageList = chatMessageService.loadMessage(roomId);
        return ResponseUtils.success("chat_message_list", chatMessageList);
    }

}
