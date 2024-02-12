package com.negongal.hummingbird.domain.user.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.Getter;

@Getter
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public class GetLoginResponse {
    private final String status;
    private final String accessToken;

    public GetLoginResponse(String status, String accessToken) {
        this.status = status;
        this.accessToken = accessToken;
    }
}
