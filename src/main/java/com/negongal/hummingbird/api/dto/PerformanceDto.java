package com.negongal.hummingbird.api.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.negongal.hummingbird.domain.Performance;
import com.negongal.hummingbird.domain.Type;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@JsonInclude(Include.NON_EMPTY)
public class PerformanceDto {
    @JsonProperty("performance_id")
    private Long id;
    private String name;
    private String artistName;
    private String photo;
    private String location;
    private Long runtime;
    private LocalDateTime date;
    private List<TicketingDto> regularTicketing;
    private List<TicketingDto> earlybirdTicketing;

    @Builder
    public PerformanceDto(Long id, String name, String artistName, String location, Long runtime,
                          LocalDateTime date, String photo, List<TicketingDto> regularTicketing, List<TicketingDto> earlybirdTicketing) {
        this.id = id;
        this.name = name;
        this.artistName = artistName;
        this.photo = photo;
        this.location = location;
        this.runtime = runtime;
        this.date = date;
        this.regularTicketing = regularTicketing;
        this.earlybirdTicketing = earlybirdTicketing;
    }

    public static PerformanceDto of(Performance p) {
        List<TicketingDto> regular = p.getTicketing().stream()
                .filter(t -> t.getType() == Type.REGULAR)
                .map(t -> TicketingDto.of(t))
                .collect(Collectors.toList());
        List<TicketingDto> earlybird = p.getTicketing().stream()
                .filter(t -> t.getType() == Type.EARLY_BIRD)
                .map(t -> TicketingDto.of(t))
                .collect(Collectors.toList());
        return PerformanceDto.builder()
                .id(p.getId())
                .name(p.getName())
                .artistName(p.getArtistName())
                .photo(p.getPhoto())
                .location(p.getLocation())
                .runtime(p.getRuntime())
                .date(p.getDate())
                .regularTicketing(regular)
                .earlybirdTicketing(earlybird)
                .build();
    }
}
