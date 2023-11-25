package com.negongal.hummingbird.domain.chat.controller;

import com.negongal.hummingbird.domain.chat.service.ChatRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequiredArgsConstructor
@RequestMapping("/chat")
public class ChatRoomController {

    private final ChatRoomService chatRoomService;

    @PostMapping("/room/{performanceId}")
    @ResponseBody
    public Long createRoom(@PathVariable Long performanceId) {
        return chatRoomService.createChatRoom(performanceId);
    }

}
