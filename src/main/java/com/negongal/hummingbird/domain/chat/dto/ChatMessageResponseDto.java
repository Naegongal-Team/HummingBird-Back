package com.negongal.hummingbird.domain.chat.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.negongal.hummingbird.domain.chat.domain.MessageType;
import com.querydsl.core.annotations.QueryProjection;

import lombok.AccessLevel;
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

	@QueryProjection
	public ChatMessageResponseDto(String nickname, MessageType type, String content,
		LocalDateTime sendTime, String profileImage) {
		this.nickname = nickname;
		this.type = type;
		this.content = content;
		this.sendTime = sendTime;
		this.profileImage = profileImage;
	}
}
