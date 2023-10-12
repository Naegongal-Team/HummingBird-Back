package com.negongal.hummingbird.api.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.negongal.hummingbird.domain.Performance;
import com.negongal.hummingbird.domain.Ticketing;
import java.time.LocalDateTime;
import java.util.List;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
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
    private List<LocalDateTime> date;

    private List<TicketingRequestDto> regularTicketing;
    private List<TicketingRequestDto> earlybirdTicketing;

    public Performance toEntity() {
        if(this.description == null) description = "";
        return Performance.builder()
                .name(name)
                .artistName(artistName)  /** Artist 매핑 필요 **/
                .location(location)
                .runtime(runtime)
                .description(description)
                .build();
    }
}