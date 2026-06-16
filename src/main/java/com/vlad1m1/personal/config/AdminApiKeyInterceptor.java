package com.vlad1m1.personal.config;

import com.vlad1m1.personal.exception.AdminUnauthorizedException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class AdminApiKeyInterceptor implements HandlerInterceptor {
    private static final String HEADER = "X-Admin-Api-Key";

    private final String adminApiKey;

    public AdminApiKeyInterceptor(@Value("${app.admin.api-key:dev-admin-api-key}") String adminApiKey) {
        this.adminApiKey = adminApiKey;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String providedKey = request.getHeader(HEADER);
        if (providedKey == null || providedKey.isBlank() || !providedKey.equals(adminApiKey)) {
            throw new AdminUnauthorizedException();
        }
        return true;
    }
}
