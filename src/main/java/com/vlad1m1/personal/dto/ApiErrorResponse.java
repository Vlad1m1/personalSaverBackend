package com.vlad1m1.personal.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.OffsetDateTime;
import java.util.Map;

@Schema(description = "Единый формат ошибки, который возвращает API.")
public record ApiErrorResponse(
        @Schema(description = "Время формирования ответа с ошибкой.", example = "2026-05-12T10:15:30+03:00", requiredMode = Schema.RequiredMode.REQUIRED)
        OffsetDateTime timestamp,

        @Schema(description = "HTTP-статус.", example = "400", requiredMode = Schema.RequiredMode.REQUIRED)
        int status,

        @Schema(description = "Краткая причина HTTP-ошибки.", example = "Bad Request", requiredMode = Schema.RequiredMode.REQUIRED)
        String error,

        @Schema(description = "Человекочитаемое сообщение для логов и инструментов разработки.", example = "Ошибка валидации", requiredMode = Schema.RequiredMode.REQUIRED)
        String message,

        @Schema(description = "Путь запроса, на котором произошла ошибка.", example = "/api/sos", requiredMode = Schema.RequiredMode.REQUIRED)
        String path,

        @Schema(description = "Идентификатор запроса для диагностики. Если клиент передал X-Request-Id, API вернет то же значение.", example = "7a2d7f2c-5c59-49a0-b0f2-2f7b2a532c4a", requiredMode = Schema.RequiredMode.REQUIRED)
        String requestId,

        @Schema(description = "Дополнительные детали ошибки: поля валидации или доменные причины.", example = "{\"contactPhone\":\"contactPhone обязателен для EMERGENCY_CONTACT\"}")
        Map<String, String> details
) {
}
