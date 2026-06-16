package com.vlad1m1.personal.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(description = "Категория для группировки памяток во Flutter-приложении.")
public record MemoCategoryResponse(
        @Schema(description = "Идентификатор категории.", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
        Long id,

        @Schema(description = "Название категории.", example = "Первая помощь", requiredMode = Schema.RequiredMode.REQUIRED)
        String name,

        @Schema(description = "Ключ Flutter-иконки или имя иконки дизайн-системы.", example = "medical_services", nullable = true)
        String iconName,

        @Schema(description = "Акцентный HEX-цвет.", example = "#D32F2F", nullable = true)
        String accentColor,

        @Schema(description = "Порядок сортировки. Меньшие значения показываются раньше.", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
        int displayOrder,

        @Schema(description = "Время последнего обновления категории для инвалидации кэша.", example = "2026-05-12T08:30:00", nullable = true)
        LocalDateTime updatedAt
) {
}
