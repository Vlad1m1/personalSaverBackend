package com.vlad1m1.personal.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Регион, доступный в мобильном приложении.")
public record RegionResponse(
        @Schema(description = "Идентификатор региона, используемый памятками, уведомлениями и SOS endpoints.", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
        Long id,

        @Schema(description = "Название региона.", example = "Москва", requiredMode = Schema.RequiredMode.REQUIRED)
        String name,

        @Schema(description = "Телефон экстренной службы региона. Если null, SOS использует 112.", example = "112", nullable = true)
        String emergencyPhone
) {
}
