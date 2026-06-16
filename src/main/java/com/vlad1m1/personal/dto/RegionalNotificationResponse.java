package com.vlad1m1.personal.dto;

import com.vlad1m1.personal.enums.NotificationSeverity;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.UUID;

@Schema(name = "RegionalNotificationResponse", description = "Региональное уведомление для мобильного списка и виджетов главного экрана.")
public record RegionalNotificationResponse(
        @Schema(description = "Идентификатор уведомления.", example = "d8df5b89-6845-4b2f-ae58-b3c6c8edc7fb", requiredMode = Schema.RequiredMode.REQUIRED)
        UUID id,

        @Schema(description = "Идентификатор региона, к которому относится уведомление.", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
        Long regionId,

        @Schema(description = "Короткий заголовок уведомления.", example = "Штормовое предупреждение", requiredMode = Schema.RequiredMode.REQUIRED)
        String title,

        @Schema(description = "Текст уведомления для деталей и push-подобного UI.", example = "Ожидается сильный ветер до 25 м/с.", requiredMode = Schema.RequiredMode.REQUIRED)
        String text,

        @Schema(description = "Важность для визуального приоритета и фильтрации.", example = "WARNING", requiredMode = Schema.RequiredMode.REQUIRED)
        NotificationSeverity severity,

        @Schema(description = "Официальное время публикации. Списки сортируются по publishedAt по убыванию.", example = "2026-05-12T08:30:00", requiredMode = Schema.RequiredMode.REQUIRED)
        LocalDateTime publishedAt,

        @Schema(description = "Время, когда backend получил или создал уведомление.", example = "2026-05-12T08:35:00", requiredMode = Schema.RequiredMode.REQUIRED)
        LocalDateTime receivedAt
) {
}
