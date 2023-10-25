package com.negongal.hummingbird.domain.performance.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@JsonInclude(Include.NON_EMPTY)
public class PerformancePageDto {

    @JsonProperty("performance_list")
    private List<PerformanceDto> performanceDto;

    private int totalPages; // 총 페이지 개수
    private Long totalElements; // 총 데이터 개수
    private boolean isLast; // 마지막 페이지인지 여부
    private int currPage; // 현재 페이지 번호

    @Builder

    public PerformancePageDto(List<PerformanceDto> performanceDto, int totalPages, Long totalElements, boolean isLast,
                              int currPage) {
        this.performanceDto = performanceDto;
        this.totalPages = totalPages;
        this.totalElements = totalElements;
        this.isLast = isLast;
        this.currPage = currPage;
    }
}
