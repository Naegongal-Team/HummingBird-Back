package com.negongal.hummingbird.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.negongal.hummingbird.domain.Performance;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
//@JsonInclude(JsonInclude.Include.NON_NULL)
public class PerformanceDto {
    @JsonProperty("performance_id")
    private Long id;
    private String name;
    private String artistName;
    private String photo;
    private String location;
    private Long runtime;
    private LocalDateTime date;

    private String ticketingLink;
    private LocalDateTime ticketingDate;

    @Builder(builderMethodName = "createPerformanceDtoBuilder")
    public PerformanceDto(Long id, String name, String artistName, String location, Long runtime,
                          LocalDateTime date, String ticketingLink, LocalDateTime ticketingDate, String photo) {
        this.id = id;
        this.name = name;
        this.artistName = artistName;
        this.photo = photo;
        this.location = location;
        this.runtime = runtime;
        this.date = date;
        this.ticketingLink = ticketingLink;
        this.ticketingDate = ticketingDate;

    }

    public static PerformanceDto of(Performance p) {
        return PerformanceDto.createPerformanceDtoBuilder()
                .id(p.getId())
                .name(p.getName())
                .artistName(p.getArtistName())
                .photo(p.getPhoto())
                .location(p.getLocation())
                .runtime(p.getRuntime())
                .date(p.getDate())
                .ticketingLink(p.getTicketingLink())
                .ticketingDate(p.getTicketingDate())
                .build();
    }
}
