package com.negongal.hummingbird.api.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.negongal.hummingbird.domain.Performance;
import com.negongal.hummingbird.domain.Ticketing;
import com.negongal.hummingbird.domain.TicketType;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class TicketingRequestDto {
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm", timezone = "Asia/Seoul")
    @JsonProperty("date")
    private LocalDateTime startDate;
    private String platform;
    private String link;
    private String description;

    public Ticketing toEntity(Performance performance, TicketType ticketType) {
        if(this.description == null) description = "";
        return Ticketing.builder()
                .performance(performance)
                .ticketType(ticketType)
                .startDate(startDate)
                .platform(platform)
                .link(link)
                .description(description)
                .build();
    }
}
