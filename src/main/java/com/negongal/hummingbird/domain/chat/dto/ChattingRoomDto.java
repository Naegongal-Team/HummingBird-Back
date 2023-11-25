package com.negongal.hummingbird.domain.chat.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.negongal.hummingbird.domain.chat.domain.ChattingRoom;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChattingRoomDto {

    @JsonProperty("chatRoom_id")
    private Long roomId;
    private Long performanceId;

    @Builder
    public ChattingRoomDto(Long roomId, Long performanceId) {
        this.roomId = roomId;
        this.performanceId = performanceId;
    }

    public static ChattingRoomDto of(ChattingRoom chattingRoom) {
        return ChattingRoomDto.builder()
                .roomId(chattingRoom.getId())
                .performanceId(chattingRoom.getPerformance().getId())
                .build();
    }
}
