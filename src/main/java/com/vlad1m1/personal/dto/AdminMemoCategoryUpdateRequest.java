package com.vlad1m1.personal.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(name = "AdminMemoCategoryUpdateRequest", description = "Административный запрос на обновление категории памяток.")
public record AdminMemoCategoryUpdateRequest(
        @Schema(description = "Обновленное название категории в каталоге памяток.", example = "Первая помощь", minLength = 1, maxLength = 120, requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "name is required")
        @Size(max = 120, message = "name must be at most 120 characters")
        String name,

        @Schema(description = "Обновленный ключ Flutter-иконки или имя иконки дизайн-системы.", example = "medical_services", maxLength = 80, nullable = true)
        @Size(max = 80, message = "iconName must be at most 80 characters")
        String iconName,

        @Schema(description = "Обновленный акцентный HEX-цвет.", example = "#D32F2F", maxLength = 16, nullable = true)
        @Size(max = 16, message = "accentColor must be at most 16 characters")
        String accentColor,

        @Schema(description = "Обновленный порядок сортировки. Меньшие значения отображаются раньше.", example = "1", minimum = "0", nullable = true)
        Integer displayOrder
) {
}
