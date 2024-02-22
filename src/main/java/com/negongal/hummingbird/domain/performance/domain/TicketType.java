package com.negongal.hummingbird.domain.performance.domain;

public enum TicketType {
	REGULAR("일반 티켓팅"), EARLY_BIRD("얼리버드 티켓팅");

	private String name;

	TicketType(String name) {
		this.name = name;
	}
}
