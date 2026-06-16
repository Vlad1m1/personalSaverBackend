package com.vlad1m1.personal.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Schema(name = "MemoDetailsResponse", description = "Полная памятка для экрана деталей. Включает htmlContent для Flutter WebView.")
public record MemoDetailResponse(
        @Schema(description = "Идентификатор памятки.", example = "8c7a15f8-2f68-4d8d-8cbb-2b26f1c4b4d3", requiredMode = Schema.RequiredMode.REQUIRED)
        UUID id,

        @Schema(description = "Идентификатор категории для группировки.", example = "1", nullable = true)
        Long categoryId,

        @Schema(description = "Идентификатор региона. null означает глобальную памятку.", example = "1", nullable = true)
        Long regionId,

        @Schema(description = "Заголовок памятки.", example = "Что делать при пожаре", requiredMode = Schema.RequiredMode.REQUIRED)
        String title,

        @Schema(description = "Стабильный slug, сгенерированный из заголовка.", example = "what-to-do-during-a-fire", requiredMode = Schema.RequiredMode.REQUIRED)
        String slug,

        @Schema(description = "Краткое описание для экрана деталей.", example = "Краткая инструкция по пожарной безопасности", nullable = true)
        String shortDescription,

        @Schema(description = "Контент HTML, который отображается во Flutter WebView.", example = "<h1>Что делать при пожаре</h1><ol><li>Покиньте здание.</li><li>Позвоните 112.</li></ol>", nullable = true)
        String htmlContent,

        @Schema(description = "Необязательные шаги для клиентов, которые не отображают HTML.", nullable = true)
        List<String> steps,

        @Schema(description = "Ключ Flutter-иконки.", example = "local_fire_department", nullable = true)
        String iconName,

        @Schema(description = "Акцентный HEX-цвет.", example = "#D32F2F", nullable = true)
        String accentColor,

        @Schema(description = "Хэш содержимого и метаданных памятки.", example = "sha256:4b4f3c0d7f9a", requiredMode = Schema.RequiredMode.REQUIRED)
        String contentHash,

        @Schema(description = "Версия контента для offline-синхронизации.", example = "3", requiredMode = Schema.RequiredMode.REQUIRED)
        int version,

        @Schema(description = "Флаг критически важного содержимого.", example = "true", requiredMode = Schema.RequiredMode.REQUIRED)
        boolean critical,

        @Schema(description = "Необязательный URL изображения.", example = "https://cdn.example.com/memos/fire.png", nullable = true)
        String imageUrl,

        @Schema(description = "Время последнего обновления. Для проверки кэша используйте contentHash/version/updatedAt.", example = "2026-05-12T10:00:00", requiredMode = Schema.RequiredMode.REQUIRED)
        LocalDateTime updatedAt
) {
}
