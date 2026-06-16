package com.vlad1m1.personal.enums;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
        description = "Regional notification severity used for filtering, visual priority, and alert styling.",
        allowableValues = {"INFO", "WARNING", "DANGER", "CRITICAL"},
        example = "WARNING"
)
public enum NotificationSeverity {
    INFO,
    WARNING,
    DANGER,
    CRITICAL
}
