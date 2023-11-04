package com.negongal.hummingbird.domain.performance.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.negongal.hummingbird.domain.performance.domain.Ticketing;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@JsonInclude(Include.NON_EMPTY)
public class TicketingDto {
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm", timezone = "Asia/Seoul")
    private LocalDateTime date;
    private String platform;
    private String link;

    @Builder
    public TicketingDto(LocalDateTime date, String platform, String link) {
        this.date = date;
        this.platform = platform;
        this.link = link;
    }

    public static TicketingDto of(Ticketing t) {
        return TicketingDto.builder()
                .date(t.getStartDate())
                .platform(t.getPlatform())
                .link(t.getLink())
                .build();
    }
}
