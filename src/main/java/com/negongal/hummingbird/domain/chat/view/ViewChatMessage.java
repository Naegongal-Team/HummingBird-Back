package com.negongal.hummingbird.domain.chat.view;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.negongal.hummingbird.domain.chat.domain.ChatMessage;
import com.negongal.hummingbird.domain.chat.domain.MessageType;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@JsonInclude(Include.NON_EMPTY)
public class ViewChatMessage {
	private String roomId;
	private String nickname;
	private MessageType type;
	private String content;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
	private LocalDateTime sendTime;

	@Builder
	public ViewChatMessage(String roomId, String nickname,
		MessageType type, String content, LocalDateTime sendTime) {
		this.roomId = roomId;
		this.nickname = nickname;
		this.type = type;
		this.content = content;
		this.sendTime = sendTime;
	}

	public static ViewChatMessage of(ChatMessage chatMessage) {
		return ViewChatMessage.builder()
			.roomId(chatMessage.getChatRoom().getRoomId())
			.nickname(chatMessage.getUser().getNickname())
			.type(chatMessage.getType())
			.content(chatMessage.getContent())
			.sendTime(chatMessage.getSendTime())
			.build();
	}
}
