package com.negongal.hummingbird.domain.chat.api;

import com.negongal.hummingbird.domain.chat.service.ChatRoomService;
import com.negongal.hummingbird.global.common.response.ApiResponse;
import com.negongal.hummingbird.global.common.response.ResponseUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/chat/room")
public class ChatRoomController {

    private final ChatRoomService chatRoomService;

    @PostMapping("/{performanceId}")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse createRoom(@PathVariable Long performanceId) {
        chatRoomService.createChatRoom(performanceId);
        return ResponseUtils.success();
    }

}
