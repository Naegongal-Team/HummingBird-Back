package com.negongal.hummingbird.global.auth.model;

import java.util.HashMap;
import java.util.Map;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class Oauth2Attributes {
	private Map<String, Object> attributes;
	private String oauth2Id;
	private String provider;

	public static Oauth2Attributes of(Map<String, Object> attributes) {
		return Oauth2Attributes.builder()
			.attributes(attributes)
			.oauth2Id((String)attributes.get("oauth2Id"))
			.provider((String)attributes.get("provider"))
			.build();
	}

	public Map<String, Object> convertToMap() {
		Map<String, Object> map = new HashMap<>();
		map.put("oauth2Id", oauth2Id);
		map.put("provider", provider);
		return map;
	}
}
