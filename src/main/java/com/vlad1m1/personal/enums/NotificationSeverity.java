package com.vlad1m1.personal.enums;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
        description = "Уровень важности регионального уведомления для цвета и приоритета в интерфейсе.",
        allowableValues = {"INFO", "WARNING", "DANGER", "CRITICAL"},
        example = "WARNING"
)
public enum NotificationSeverity {
    INFO,
    WARNING,
    DANGER,
    CRITICAL
}
