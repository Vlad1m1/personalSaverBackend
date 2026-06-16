package com.vlad1m1.personal.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.UUID;

@Schema(name = "MemoListResponse", description = "Элемент памятки для каталога и списков обновлений. htmlContent намеренно не возвращается.")
public record MemoSummaryResponse(
        @Schema(description = "Идентификатор памятки для GET /api/memos/{id}.", example = "8c7a15f8-2f68-4d8d-8cbb-2b26f1c4b4d3", requiredMode = Schema.RequiredMode.REQUIRED)
        UUID id,

        @Schema(description = "Идентификатор категории для группировки.", example = "1", nullable = true)
        Long categoryId,

        @Schema(description = "Идентификатор региона. null означает глобальную памятку для всех регионов.", example = "1", nullable = true)
        Long regionId,

        @Schema(description = "Заголовок памятки.", example = "Что делать при пожаре", requiredMode = Schema.RequiredMode.REQUIRED)
        String title,

        @Schema(description = "Стабильный slug, сгенерированный из заголовка для кэша клиента и аналитики.", example = "what-to-do-during-a-fire", requiredMode = Schema.RequiredMode.REQUIRED)
        String slug,

        @Schema(description = "Краткое описание для каталога.", example = "Краткая инструкция по пожарной безопасности", nullable = true)
        String shortDescription,

        @Schema(description = "Ключ Flutter-иконки конкретной памятки. Клиент должен использовать его перед fallback-иконкой категории.", example = "local_fire_department", nullable = true)
        String iconName,

        @Schema(description = "Акцентный HEX-цвет конкретной памятки. Если отсутствует, клиент может использовать цвет категории.", example = "#D84315", nullable = true)
        String accentColor,

        @Schema(description = "Необязательный URL изображения памятки для карточек и деталей.", example = "https://cdn.example.com/memos/fire.png", nullable = true)
        String imageUrl,

        @Schema(description = "Хэш содержимого и метаданных памятки. Используется для проверки изменений кэша.", example = "sha256:4b4f3c0d7f9a", requiredMode = Schema.RequiredMode.REQUIRED)
        String contentHash,

        @Schema(description = "Версия контента для offline-синхронизации.", example = "3", requiredMode = Schema.RequiredMode.REQUIRED)
        int version,

        @Schema(description = "Время последнего обновления, используемое GET /api/memos/updates?since=...", example = "2026-05-12T10:00:00", requiredMode = Schema.RequiredMode.REQUIRED)
        LocalDateTime updatedAt
) {
}
