package com.negongal.hummingbird.domain;

public enum Type {
    REGULAR("일반 티켓팅"), EARLY_BIRD("얼리버드 티켓팅");

    private String name;

    Type(String name) {
        this.name = name;
    }
}
