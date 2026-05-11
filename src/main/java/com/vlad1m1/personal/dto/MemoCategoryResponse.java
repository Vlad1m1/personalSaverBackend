package com.vlad1m1.personal.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(description = "Категория для группировки памяток в интерфейсе Flutter-приложения.")
public record MemoCategoryResponse(
        @Schema(description = "Идентификатор категории.", example = "10", requiredMode = Schema.RequiredMode.REQUIRED)
        Long id,

        @Schema(description = "Название категории в каталоге памяток.", example = "Первая помощь", requiredMode = Schema.RequiredMode.REQUIRED)
        String name,

        @Schema(description = "Имя Flutter-иконки или ключ иконки из дизайн-системы.", example = "medical_services")
        String iconName,

        @Schema(description = "HEX-цвет для акцента категории в мобильном интерфейсе.", example = "#D32F2F")
        String accentColor,

        @Schema(description = "Порядок сортировки. Меньшие значения отображаются раньше.", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
        int displayOrder,

        @Schema(description = "Время последнего изменения категории для инвалидации офлайн-кэша.", example = "2026-05-12T08:30:00")
        LocalDateTime updatedAt
) {
}
