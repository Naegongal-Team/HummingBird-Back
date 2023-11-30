package com.negongal.hummingbird.domain.performance.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.negongal.hummingbird.domain.performance.domain.Performance;
import com.negongal.hummingbird.domain.performance.domain.TicketType;
import java.time.LocalDateTime;
import java.util.Comparator;
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
public class PerformanceDetailDto {
    @JsonProperty("performance_id")
    private Long id;
    private String name;
    private String artistName;
    private String photo;
    private String location;
    private String description;
    private Long runtime;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm", timezone = "Asia/Seoul")
    private List<LocalDateTime> date;
    private List<TicketingDto> regularTicketing;
    private List<TicketingDto> earlybirdTicketing;

    @JsonProperty("is_heart_pressed")
    private boolean heartPressed;
    private boolean past;

    private String roomId;

    @Builder
    public PerformanceDetailDto(Long id, String name, String artistName, String location, Long runtime, String description, boolean heartPressed,
                                List<LocalDateTime> date, String photo, List<TicketingDto> regularTicketing, List<TicketingDto> earlybirdTicketing,
                                String roomId) {
        this.id = id;
        this.name = name;
        this.artistName = artistName;
        this.photo = photo;
        this.location = location;
        this.runtime = runtime;
        this.description = description;
        this.date = date;
        this.regularTicketing = regularTicketing;
        this.earlybirdTicketing = earlybirdTicketing;

        this.past = (date.get(date.size() - 1).isBefore(LocalDateTime.now())) ? true : false;
        this.heartPressed = heartPressed;
        this.roomId = roomId;
    }

    public static PerformanceDetailDto of(Performance p, boolean heartPressed) {
        List<LocalDateTime> dateList = p.getDateList().stream().map(d -> d.getStartDate()).sorted().collect(Collectors.toList());
        return PerformanceDetailDto.builder()
                .id(p.getId())
                .name(p.getName())
                .artistName(p.getArtist().getName())
                .photo(p.getPhoto())
                .location(p.getLocation())
                .runtime(p.getRuntime())
                .description(p.getDescription())
                .date(dateList)
                .regularTicketing(p.getTicketingList().stream()
                        .filter(t -> t.getTicketType() == TicketType.REGULAR)
                        .map(t -> TicketingDto.of(t))
                        .sorted(Comparator.comparing(TicketingDto::getDate))
                        .collect(Collectors.toList()))
                .earlybirdTicketing(p.getTicketingList().stream()
                        .filter(t -> t.getTicketType() == TicketType.EARLY_BIRD)
                        .map(t -> TicketingDto.of(t))
                        .sorted(Comparator.comparing(TicketingDto::getDate))
                        .collect(Collectors.toList()))
                .heartPressed(heartPressed)
                .roomId(p.getChatRoom().getRoomId())
                .build();
    }
}
