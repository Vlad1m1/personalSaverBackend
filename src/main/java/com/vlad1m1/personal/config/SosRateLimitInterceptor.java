package com.vlad1m1.personal.config;

import com.vlad1m1.personal.exception.RateLimitExceededException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class SosRateLimitInterceptor implements HandlerInterceptor {
    private final Map<String, Deque<Instant>> requestsByIp = new ConcurrentHashMap<>();
    private final int limitPerMinute;
    private final Clock clock;

    public SosRateLimitInterceptor(@Value("${app.sos.rate-limit-per-minute:5}") int limitPerMinute) {
        this.limitPerMinute = limitPerMinute;
        this.clock = Clock.systemUTC();
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if (!"POST".equalsIgnoreCase(request.getMethod())) {
            return true;
        }

        String clientIp = clientIp(request);
        Instant now = Instant.now(clock);
        Instant windowStart = now.minus(Duration.ofMinutes(1));
        Deque<Instant> timestamps = requestsByIp.computeIfAbsent(clientIp, ignored -> new ArrayDeque<>());

        synchronized (timestamps) {
            while (!timestamps.isEmpty() && timestamps.peekFirst().isBefore(windowStart)) {
                timestamps.removeFirst();
            }
            if (timestamps.size() >= limitPerMinute) {
                throw new RateLimitExceededException("Too many SOS requests from this IP address");
            }
            timestamps.addLast(now);
        }
        return true;
    }

    private String clientIp(HttpServletRequest request) {
        String forwardedFor = request.getHeader("X-Forwarded-For");
        if (forwardedFor != null && !forwardedFor.isBlank()) {
            return forwardedFor.split(",")[0].trim();
        }
        return request.getRemoteAddr();
    }
}
