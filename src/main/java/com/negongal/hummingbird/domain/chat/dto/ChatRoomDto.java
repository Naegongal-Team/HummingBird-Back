package com.negongal.hummingbird.domain.chat.dto;

import java.io.Serializable;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ChatRoomDto implements Serializable {
    private static final long serialVersionUID = 6494678977089006639L;

    private String roomId;

    @Builder
    public ChatRoomDto(String roomId) {
        this.roomId = roomId;
    }

    public static ChatRoomDto create() {
        String roomId = UUID.randomUUID().toString();
        return ChatRoomDto.builder()
                .roomId(roomId)
                .build();
    }
}
