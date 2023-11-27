package com.negongal.hummingbird.domain.chat.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.negongal.hummingbird.domain.chat.domain.ChatMessage;
import com.negongal.hummingbird.domain.chat.domain.MessageType;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@JsonInclude(Include.NON_EMPTY)
public class ChatMessageResponseDto {
    private String nickname;
    private MessageType type;
    private String content;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime sendTime;

    private String profileImage;
    @JsonProperty("is_sent")
    private boolean sent;

    @Builder
    public ChatMessageResponseDto(String nickname, MessageType type, String content,
                                  LocalDateTime sendTime, String profileImage, boolean sent) {
        this.nickname = nickname;
        this.type = type;
        this.content = content;
        this.sendTime = sendTime;
        this.profileImage = profileImage;
        this.sent = sent;
    }

    public static ChatMessageResponseDto of(ChatMessage chatMessage, Long finalLoginUserId) {
        boolean isSent = false;
        if(finalLoginUserId != null && chatMessage.getUser().getUserId() == finalLoginUserId) {
            isSent = true;
        }
        return ChatMessageResponseDto.builder()
                .nickname(chatMessage.getUser().getNickname())
                .type(chatMessage.getType())
                .content(chatMessage.getContent())
                .sendTime(chatMessage.getSendTime())
                .profileImage(chatMessage.getUser().getProfileImage())
                .sent(isSent)
                .build();
    }
}
