package com.negongal.hummingbird.domain.chat.view;

import com.negongal.hummingbird.domain.chat.domain.ChatRoom;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ViewChatRoom {
    private Long performanceId;
    private String performanceName;
    private String roomId;

    @Builder
    public ViewChatRoom(Long performanceId, String performanceName, String roomId) {
        this.performanceId = performanceId;
        this.performanceName = performanceName;
        this.roomId = roomId;
    }

    public static ViewChatRoom of(ChatRoom room) {
        return ViewChatRoom.builder()
                .performanceId(room.getPerformance().getId())
                .performanceName(room.getPerformance().getName())
                .roomId(room.getRoomId())
                .build();
    }
}
