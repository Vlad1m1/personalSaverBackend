package com.vlad1m1.personal.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.List;

@Schema(name = "AdminMemoCreateRequest", description = "Административный запрос на создание памятки. Новую памятку можно оставить неопубликованной через active=false.")
public record AdminMemoCreateRequest(
        @Schema(description = "Идентификатор категории памятки.", example = "1", nullable = true)
        Long categoryId,

        @Schema(description = "Идентификатор региона для региональной памятки. null означает глобальную памятку.", example = "1", nullable = true)
        Long regionId,

        @Schema(description = "Заголовок памятки для списка и экрана деталей.", example = "Что делать при пожаре", minLength = 1, maxLength = 160, requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "title is required")
        @Size(max = 160, message = "title must be at most 160 characters")
        String title,

        @Schema(description = "Краткое описание для списка. htmlContent не возвращается в GET /api/memos.", example = "Краткая инструкция по пожарной безопасности", maxLength = 500, requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "shortDescription is required")
        @Size(max = 500, message = "shortDescription must be at most 500 characters")
        String shortDescription,

        @Schema(description = "Тело HTML для Flutter WebView на экране деталей памятки.", example = "<h1>Что делать при пожаре</h1><ol><li>Покиньте здание.</li><li>Позвоните 112.</li></ol>", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "htmlContent is required")
        String htmlContent,

        @Schema(description = "Необязательные структурированные шаги для native-отображения.", nullable = true)
        List<String> steps,

        @Schema(description = "Версия контента для offline-синхронизации.", example = "3", minimum = "1", nullable = true)
        @Min(value = 1, message = "version must be greater than or equal to 1")
        Integer version,

        @Schema(description = "Помечает критически важный safety-контент.", example = "true", nullable = true)
        Boolean critical,

        @Schema(description = "Необязательный URL изображения для мобильного приложения.", example = "https://cdn.example.com/memos/fire.png", nullable = true)
        String imageUrl,

        @Schema(description = "Необязательный ключ Flutter-иконки.", example = "local_fire_department", nullable = true)
        String iconName,

        @Schema(description = "Необязательный акцентный HEX-цвет.", example = "#D32F2F", nullable = true)
        String accentColor,

        @Schema(description = "Флаг публикации. false создает черновик; publish endpoint устанавливает true.", example = "false", nullable = true)
        Boolean active
) {
}
