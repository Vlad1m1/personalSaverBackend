package com.vlad1m1.personal.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.OffsetDateTime;
import java.util.List;

@Schema(name = "ApiErrorResponse", description = "Единый формат ошибки для публичных и admin API endpoints.")
public record ApiErrorResponse(
        @Schema(description = "Метка времени UTC или timestamp с offset сервера, когда был сформирован ответ с ошибкой.", example = "2026-05-12T10:00:00Z", requiredMode = Schema.RequiredMode.REQUIRED)
        OffsetDateTime timestamp,

        @Schema(description = "Код HTTP-статуса.", example = "400", minimum = "100", maximum = "599", requiredMode = Schema.RequiredMode.REQUIRED)
        int status,

        @Schema(description = "Краткая HTTP-фраза причины.", example = "Bad Request", requiredMode = Schema.RequiredMode.REQUIRED)
        String error,

        @Schema(description = "Читаемое сообщение об ошибке для логов и developer tooling.", example = "Validation error", requiredMode = Schema.RequiredMode.REQUIRED)
        String message,

        @Schema(description = "Путь запроса, на котором произошла ошибка.", example = "/api/sos", requiredMode = Schema.RequiredMode.REQUIRED)
        String path,

        @Schema(description = "Корреляционный id запроса. Если был передан X-Request-Id, возвращается то же значение.", example = "7f3c2d8a", requiredMode = Schema.RequiredMode.REQUIRED)
        String requestId,

        @Schema(description = "Детали ошибок по полям или доменным правилам. Пустой массив означает отсутствие структурированных деталей.", requiredMode = Schema.RequiredMode.REQUIRED)
        List<ApiErrorDetail> details
) {
    @Schema(name = "ApiErrorDetail", description = "Одна деталь ошибки валидации или доменного правила.")
    public record ApiErrorDetail(
            @Schema(description = "Поле, параметр или доменный объект, вызвавший ошибку.", example = "latitude", nullable = true)
            String field,

            @Schema(description = "Подробное сообщение для разработчика.", example = "must be greater than or equal to -90", requiredMode = Schema.RequiredMode.REQUIRED)
            String message
    ) {
    }
}
