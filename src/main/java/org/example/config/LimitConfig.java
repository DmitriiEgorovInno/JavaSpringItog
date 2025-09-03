package org.example.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableConfigurationProperties(LimitProperties.class)
public class LimitConfig {
    private final LimitProperties limitProperties;

    public LimitConfig(LimitProperties limitProperties) {
        this.limitProperties = limitProperties;
    }
}
