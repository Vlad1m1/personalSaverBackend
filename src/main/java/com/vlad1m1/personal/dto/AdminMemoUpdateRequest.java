package com.vlad1m1.personal.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.List;

@Schema(name = "AdminMemoUpdateRequest", description = "Административный запрос на замену редактируемых полей памятки.")
public record AdminMemoUpdateRequest(
        @Schema(description = "Обновленный id категории памятки.", example = "1", nullable = true)
        Long categoryId,

        @Schema(description = "Обновленный id региона. Null означает глобальную памятку.", example = "1", nullable = true)
        Long regionId,

        @Schema(description = "Обновленный заголовок памятки.", example = "Что делать при пожаре", minLength = 1, maxLength = 160, requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "title is required")
        @Size(max = 160, message = "title must be at most 160 characters")
        String title,

        @Schema(description = "Обновленное краткое описание для списка.", example = "Краткая инструкция по пожарной безопасности", maxLength = 500, requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "shortDescription is required")
        @Size(max = 500, message = "shortDescription must be at most 500 characters")
        String shortDescription,

        @Schema(description = "Обновленное HTML-тело для Flutter WebView.", example = "<h1>Что делать при пожаре</h1><ol><li>Покиньте здание.</li><li>Позвоните 112.</li></ol>", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "htmlContent is required")
        String htmlContent,

        @Schema(description = "Обновленные необязательные native-шаги.", nullable = true)
        List<String> steps,

        @Schema(description = "Обновленная версия контента для offline-синхронизации.", example = "4", minimum = "1", nullable = true)
        @Min(value = 1, message = "version must be greater than or equal to 1")
        Integer version,

        @Schema(description = "Обновленный флаг критически важного контента.", example = "true", nullable = true)
        Boolean critical,

        @Schema(description = "Обновленный необязательный URL изображения.", example = "https://cdn.example.com/memos/fire.png", nullable = true)
        String imageUrl,

        @Schema(description = "Обновленный необязательный ключ Flutter-иконки.", example = "local_fire_department", nullable = true)
        String iconName,

        @Schema(description = "Обновленный необязательный акцентный HEX-цвет.", example = "#D32F2F", nullable = true)
        String accentColor,

        @Schema(description = "Флаг публикации. Для явных переходов workflow используйте publish/unpublish endpoints.", example = "true", nullable = true)
        Boolean active
) {
}
