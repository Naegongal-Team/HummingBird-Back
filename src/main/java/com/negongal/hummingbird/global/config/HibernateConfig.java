package com.negongal.hummingbird.global.config;

import org.hibernate.cfg.AvailableSettings;
import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.negongal.hummingbird.global.common.QueryCountInspector;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Configuration
public class HibernateConfig {

	private final QueryCountInspector queryCountInspector;

	@Bean
	public HibernatePropertiesCustomizer configureStatementInspector() {
		return hibernateProperties -> hibernateProperties.put(AvailableSettings.STATEMENT_INSPECTOR,
			queryCountInspector);
	}
}
