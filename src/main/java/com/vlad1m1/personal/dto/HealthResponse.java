package com.vlad1m1.personal.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.OffsetDateTime;

@Schema(description = "Базовый статус сервиса для smoke-проверок.")
public record HealthResponse(
        @Schema(description = "Текущий статус сервиса.", example = "UP", requiredMode = Schema.RequiredMode.REQUIRED)
        String status,

        @Schema(description = "Имя сервиса.", example = "personal-rescue-backend", requiredMode = Schema.RequiredMode.REQUIRED)
        String service,

        @Schema(description = "Время сервера на момент формирования ответа.", example = "2026-05-12T10:15:30+03:00", requiredMode = Schema.RequiredMode.REQUIRED)
        OffsetDateTime timestamp
) {
}
