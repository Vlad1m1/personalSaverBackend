package com.vlad1m1.personal.enums;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
        description = "Важность регионального уведомления для фильтрации, визуального приоритета и оформления.",
        allowableValues = {"INFO", "WARNING", "DANGER", "CRITICAL"},
        example = "WARNING"
)
public enum NotificationSeverity {
    INFO,
    WARNING,
    DANGER,
    CRITICAL
}
