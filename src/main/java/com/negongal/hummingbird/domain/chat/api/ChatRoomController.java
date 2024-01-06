package com.negongal.hummingbird.domain.chat.api;

import com.negongal.hummingbird.domain.chat.dto.ChatMessagePageDto;
import com.negongal.hummingbird.domain.chat.dto.ChatMessageResponseDto;
import com.negongal.hummingbird.domain.chat.service.ChatMessageService;
import com.negongal.hummingbird.global.common.response.ApiResponse;
import com.negongal.hummingbird.global.common.response.ResponseUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "ChatRoom API", description = "")
@RestController
@RequiredArgsConstructor
@RequestMapping("/chat/room")
public class ChatRoomController {

    private final ChatMessageService chatMessageService;

    /**
     * 채팅방 메시지 내역 조회
     */
    @Operation(summary = "채팅방 메세지 리스트 조회", description = "공연 채팅방에 대한 전체 메시지 리스트를 조회합니다.")
    @Parameter(name = "roomId", description = "공연 채팅방 아이디 값", example = "roomId")
    @GetMapping("/{roomId}/message")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<?> findAllMessage(@PathVariable String roomId, Pageable pageable) {
        ChatMessagePageDto chatMessagePageList = chatMessageService.loadMessage(roomId, pageable);
        return ResponseUtils.success(chatMessagePageList);
    }

}
