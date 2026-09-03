package com.victorpolicarpo.task_manager.security;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Getter
@Setter
@Validated
@ConfigurationProperties(prefix = "jwt")
public class JwtProperties {

    @NotBlank(message = "JWT_SECRET must be configured.")
    private String secret;

    @Min(value = 1, message = "JWT_EXPIRATION must be greater than zero.")
    private long expiration;
}
