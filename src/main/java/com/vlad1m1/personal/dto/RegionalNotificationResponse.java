package com.vlad1m1.personal.dto;

import com.vlad1m1.personal.enums.NotificationSeverity;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.UUID;

@Schema(name = "RegionalNotificationResponse", description = "Regional notification displayed in mobile list and home screen widgets.")
public record RegionalNotificationResponse(
        @Schema(description = "Notification id.", example = "d8df5b89-6845-4b2f-ae58-b3c6c8edc7fb", requiredMode = Schema.RequiredMode.REQUIRED)
        UUID id,

        @Schema(description = "Region id this notification belongs to.", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
        Long regionId,

        @Schema(description = "Short notification title.", example = "Storm warning", requiredMode = Schema.RequiredMode.REQUIRED)
        String title,

        @Schema(description = "Notification text for details and push-like UI.", example = "Strong wind up to 25 m/s is expected.", requiredMode = Schema.RequiredMode.REQUIRED)
        String text,

        @Schema(description = "Severity used for visual priority and filtering.", example = "WARNING", requiredMode = Schema.RequiredMode.REQUIRED)
        NotificationSeverity severity,

        @Schema(description = "Official publication timestamp. Lists are sorted by publishedAt descending, with receivedAt as a fallback.", example = "2026-05-12T08:30:00", requiredMode = Schema.RequiredMode.REQUIRED)
        LocalDateTime publishedAt,

        @Schema(description = "Timestamp when the backend received or created the notification.", example = "2026-05-12T08:35:00", requiredMode = Schema.RequiredMode.REQUIRED)
        LocalDateTime receivedAt
) {
}
