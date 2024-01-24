package com.negongal.hummingbird.domain.performance.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.negongal.hummingbird.domain.artist.domain.Artist;
import com.negongal.hummingbird.domain.performance.domain.Performance;
import java.time.LocalDateTime;
import java.util.List;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class PerformanceRequestDto {
    @NotBlank(message = "공연 이름은 필수 값입니다.")
    private String name;

    @NotBlank(message = "가수 이름은 필수 값입니다.")
    private String artistName;

    @NotBlank(message = "장소는 필수 값입니다.")
    private String location;

    private String description;

    @NotNull(message = "공연 관람 시간은 필수 값입니다.")
    private Long runtime;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm", timezone = "Asia/Seoul")
    @JsonProperty("date")
    private List<LocalDateTime> dateList;

    private List<TicketingRequestDto> regularTicketing;
    private List<TicketingRequestDto> earlybirdTicketing;

    public Performance toEntity(Artist artist) {
        if(this.description == null) description = "";
        return Performance.builder()
                .name(name)
                .artist(artist)
                .location(location)
                .runtime(runtime)
                .description(description)
                .build();
    }
}
