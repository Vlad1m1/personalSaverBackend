package com.vlad1m1.personal.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Schema(name = "AlarmRequest", description = "Запрос на создание или обновление тревожного уведомления региона.")
public record AlarmRequest(
        @Schema(description = "Идентификатор региона, к которому относится уведомление.", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotNull(message = "regionId is required")
        Long regionId,

        @Schema(description = "Подробный текст уведомления: что произошло, что делать пользователю и каких действий избегать.", example = "В вашем регионе объявлено экстренное уведомление. Сохраняйте спокойствие, проверяйте официальные источники информации и следуйте инструкциям местных служб.", minLength = 1, maxLength = 4000, requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "text is required")
        @Size(max = 4000, message = "text must be at most 4000 characters")
        String text
) {
}
