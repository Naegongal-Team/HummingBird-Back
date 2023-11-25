package com.negongal.hummingbird.domain.chat.dto;

import com.negongal.hummingbird.domain.chat.domain.MessageType;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatMessageDto {
    private String roomId;
    private String nickname;
    private MessageType type;
    private String content;

    public void setEnterMessage() {
        this.content = this.nickname + "님이 입장하셨습니다.";
    }

    @Override
    public String toString() {
        return "ChatMessageDto{" +
                "roomId='" + roomId + '\'' +
                ", nickname='" + nickname + '\'' +
                ", type=" + type +
                ", content='" + content + '\'' +
                '}';
    }
}
