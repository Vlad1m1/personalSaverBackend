package com.vlad1m1.personal.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Регион, доступный в мобильном приложении.")
public record RegionResponse(
        @Schema(description = "Идентификатор региона для памяток, уведомлений и SOS endpoint'ов.", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
        Long id,

        @Schema(description = "Название региона для отображения в приложении.", example = "Москва", requiredMode = Schema.RequiredMode.REQUIRED)
        String name,

        @Schema(description = "Номер экстренной службы региона. Если значение отсутствует, SOS использует 112.", example = "112")
        String emergencyPhone
) {
}
