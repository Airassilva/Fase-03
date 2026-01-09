package dev.aira.historico.configs;

import feign.RequestInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignSecurityConfig {

    @Value("${service.jwt.token}")
    private String serviceToken;

    @Bean
    public RequestInterceptor serviceAuthInterceptor() {
        return template -> template.header(
                "Authorization",
                "Bearer " + serviceToken
        );
    }
}
