package com.vlad1m1.personal.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.OffsetDateTime;

@Schema(name = "NotificationParsingLogResponse", description = "Результат запуска парсинга региональных уведомлений, видимый администратору.")
public record NotificationParsingLogResponse(
        @Schema(description = "Идентификатор записи журнала. Для текущего in-memory парсера это timestamp запуска.", example = "2026-05-12T10:00:00Z", requiredMode = Schema.RequiredMode.REQUIRED)
        String id,

        @Schema(description = "Время начала парсинга.", example = "2026-05-12T10:00:00Z", requiredMode = Schema.RequiredMode.REQUIRED)
        OffsetDateTime startedAt,

        @Schema(description = "Время завершения парсинга.", example = "2026-05-12T10:00:03Z", nullable = true)
        OffsetDateTime finishedAt,

        @Schema(description = "Статус запуска.", example = "SUCCESS", allowableValues = {"SUCCESS", "FAILED"}, requiredMode = Schema.RequiredMode.REQUIRED)
        String status,

        @Schema(description = "Источник, который инициировал парсинг.", example = "manual", requiredMode = Schema.RequiredMode.REQUIRED)
        String source,

        @Schema(description = "Короткое сообщение для оператора.", example = "Manual notification parsing completed", requiredMode = Schema.RequiredMode.REQUIRED)
        String message,

        @Schema(description = "Количество созданных уведомлений.", example = "2", minimum = "0", requiredMode = Schema.RequiredMode.REQUIRED)
        int createdCount,

        @Schema(description = "Количество обновленных уведомлений.", example = "1", minimum = "0", requiredMode = Schema.RequiredMode.REQUIRED)
        int updatedCount,

        @Schema(description = "Детали ошибки, если парсинг завершился неуспешно.", example = "Source timeout", nullable = true)
        String errorDetails
) {
}
