package com.vlad1m1.personal.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.UUID;

@Schema(description = "Памятка для экранов каталога. htmlContent намеренно не возвращается в списке.")
public record MemoSummaryResponse(
        @Schema(description = "Идентификатор памятки.", example = "8c7a15f8-2f68-4d8d-8cbb-2b26f1c4b4d3", requiredMode = Schema.RequiredMode.REQUIRED)
        UUID id,

        @Schema(description = "Идентификатор категории для группировки в интерфейсе.", example = "10")
        Long categoryId,

        @Schema(description = "Идентификатор региона для региональных памяток. Null означает общую памятку для всех регионов.", example = "1")
        Long regionId,

        @Schema(description = "Заголовок памятки в списке.", example = "Что делать при пожаре", requiredMode = Schema.RequiredMode.REQUIRED)
        String title,

        @Schema(description = "Краткое описание для карточек и результатов поиска.", example = "Основные шаги, чтобы защитить себя и вызвать экстренные службы.")
        String shortDescription,

        @Schema(description = "Версия содержимого памятки для офлайн-синхронизации.", example = "3", requiredMode = Schema.RequiredMode.REQUIRED)
        int version,

        @Schema(description = "Нужно ли выделять памятку как критически важную.", example = "true", requiredMode = Schema.RequiredMode.REQUIRED)
        boolean critical,

        @Schema(description = "Необязательная ссылка на изображение для превью памятки.", example = "https://cdn.example.com/memos/fire.png")
        String imageUrl,

        @Schema(description = "Время последнего обновления памятки для офлайн-синхронизации.", example = "2026-05-12T08:30:00")
        LocalDateTime updatedAt
) {
}
