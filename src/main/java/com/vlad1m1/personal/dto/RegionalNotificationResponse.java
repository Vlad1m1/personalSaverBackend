package com.vlad1m1.personal.dto;

import com.vlad1m1.personal.enums.NotificationSeverity;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.UUID;

@Schema(description = "Уведомление для пользователей выбранного региона.")
public record RegionalNotificationResponse(
        @Schema(description = "Идентификатор уведомления.", example = "d8df5b89-6845-4b2f-ae58-b3c6c8edc7fb", requiredMode = Schema.RequiredMode.REQUIRED)
        UUID id,

        @Schema(description = "Идентификатор региона, к которому относится уведомление.", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
        Long regionId,

        @Schema(description = "Короткий заголовок уведомления.", example = "Предупреждение о сильном дожде", requiredMode = Schema.RequiredMode.REQUIRED)
        String title,

        @Schema(description = "Текст сообщения для экрана деталей уведомления.", example = "Избегайте низин и следуйте рекомендациям местных экстренных служб.", requiredMode = Schema.RequiredMode.REQUIRED)
        String message,

        @Schema(description = "Уровень важности, который влияет на цвет и приоритет в мобильном интерфейсе.", example = "WARNING", requiredMode = Schema.RequiredMode.REQUIRED)
        NotificationSeverity severity,

        @Schema(description = "Время публикации уведомления.", example = "2026-05-12T08:30:00", requiredMode = Schema.RequiredMode.REQUIRED)
        LocalDateTime publishedAt
) {
}
