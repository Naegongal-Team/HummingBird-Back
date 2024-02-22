package com.negongal.hummingbird.global.common;

import lombok.Getter;

@Getter
public enum CacheType {
	ARTISTS("artists", 24 * 60 * 60, 5000000);

	CacheType(String cacheName, int expiredAfterWrite, int maximumSize) {
		this.cacheName = cacheName;
		this.expiredAfterWrite = expiredAfterWrite;
		this.maximumSize = maximumSize;
	}

	private String cacheName;
	private int expiredAfterWrite;
	private int maximumSize;
}
