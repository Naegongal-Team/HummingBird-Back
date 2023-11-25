package com.negongal.hummingbird.domain.chat.dto;

import lombok.*;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChattingMessageDto {

    private Long roomId;
    private Long senderId;
    private String content;
}
