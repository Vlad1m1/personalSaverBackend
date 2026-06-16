package com.vlad1m1.personal.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    private final AdminApiKeyInterceptor adminApiKeyInterceptor;
    private final SosRateLimitInterceptor sosRateLimitInterceptor;

    public WebConfig(AdminApiKeyInterceptor adminApiKeyInterceptor, SosRateLimitInterceptor sosRateLimitInterceptor) {
        this.adminApiKeyInterceptor = adminApiKeyInterceptor;
        this.sosRateLimitInterceptor = sosRateLimitInterceptor;
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
                .allowedOriginPatterns("*")
                .allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .exposedHeaders("Location");
    }

    @Override
    public void addInterceptors(org.springframework.web.servlet.config.annotation.InterceptorRegistry registry) {
        registry.addInterceptor(adminApiKeyInterceptor).addPathPatterns("/api/admin/**");
        registry.addInterceptor(sosRateLimitInterceptor).addPathPatterns("/api/sos");
    }
}
