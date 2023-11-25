package com.negongal.hummingbird.domain.chat.api;

import com.negongal.hummingbird.domain.chat.application.ChattingService;
import com.negongal.hummingbird.domain.chat.dto.ChattingRoomDto;
import com.negongal.hummingbird.global.common.response.ApiResponse;
import com.negongal.hummingbird.global.common.response.ResponseUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;


@RequiredArgsConstructor
@RestController
@RequestMapping("/chatting")
public class ChattingRoomApiController {

    private final ChattingService chattingService;

    // 채팅방 생성
    @PostMapping("/room/{performanceId}")
    public ApiResponse<?> create(@PathVariable Long performanceId) {
        chattingService.save(performanceId);
        return ResponseUtils.success();
    }


    // 특정 채팅방 조회
    @GetMapping("/room/{roomId}")
    public ApiResponse<?> roomInfo(@PathVariable Long roomId) {
        ChattingRoomDto chattingRoomDto = chattingService.findOne(roomId);
        return ResponseUtils.success(chattingRoomDto);
    }

}