package com.test.config;

import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Getter;

@Component
@ConfigurationProperties(prefix = "spring.datasource")
@Getter
public class DataSourceConfigProperties {
	private Map<String, String> datasourceProps;
}
