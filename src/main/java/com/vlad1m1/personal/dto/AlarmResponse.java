package com.vlad1m1.personal.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

@Schema(name = "AlarmResponse", description = "Тревожное уведомление, привязанное к региону.")
public record AlarmResponse(
        @Schema(description = "Идентификатор уведомления.", example = "5f2f8715-2c4f-4ab8-a450-43e693d6641d", requiredMode = Schema.RequiredMode.REQUIRED)
        UUID id,

        @Schema(description = "Идентификатор региона.", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
        Long regionId,

        @Schema(description = "Подробный текст уведомления.", example = "В вашем регионе объявлено экстренное уведомление. Сохраняйте спокойствие, проверяйте официальные источники информации и следуйте инструкциям местных служб.", requiredMode = Schema.RequiredMode.REQUIRED)
        String text
) {
}
