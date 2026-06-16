package com.vlad1m1.personal.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.OffsetDateTime;

@Schema(description = "Базовый статус сервиса для smoke-check проверок.")
public record HealthResponse(
        @Schema(description = "Текущий статус сервиса.", example = "UP", requiredMode = Schema.RequiredMode.REQUIRED)
        String status,

        @Schema(description = "Имя сервиса.", example = "personal-rescue-backend", requiredMode = Schema.RequiredMode.REQUIRED)
        String service,

        @Schema(description = "Серверное время формирования ответа.", example = "2026-05-12T10:15:30Z", requiredMode = Schema.RequiredMode.REQUIRED)
        OffsetDateTime timestamp
) {
}
