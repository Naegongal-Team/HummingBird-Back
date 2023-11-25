package com.negongal.hummingbird.domain.chat.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.negongal.hummingbird.domain.chat.domain.MessageType;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatMessageDto {
    private String roomId;
    private String sender;
    private MessageType type;
    private String message;

    public void setEnterMessage() {
        this.message = this.sender + "님이 입장하셨습니다.";
    }

    @Override
    public String toString() {
        return "ChatMessageDto{" +
                "roomId='" + roomId + '\'' +
                ", sender='" + sender + '\'' +
                ", type=" + type +
                ", message='" + message + '\'' +
                '}';
    }
}
