package com.company.treasury.security.jwt;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "jwt")
public class JwtProperties {
    private String secret = "treasure-platform-default-secret-key-change-in-production";
    private long expiration = 86400000;
}
