package com.vlad1m1.personal.dto;

import com.vlad1m1.personal.enums.NotificationSeverity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record RegionalNotificationRequest(
        @NotNull(message = "regionId is required")
        Long regionId,
        @NotBlank(message = "title is required")
        String title,
        @NotBlank(message = "message is required")
        String message,
        @NotNull(message = "severity is required")
        NotificationSeverity severity,
        LocalDateTime publishedAt,
        Boolean active
) {
}
