package com.hilo.service.api.util;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix="config.define")
@PropertySource("classpath:config.properties")
@Data
public class ConfigProperties {
    private String accessKeyId;
    private String secretKey;
}
