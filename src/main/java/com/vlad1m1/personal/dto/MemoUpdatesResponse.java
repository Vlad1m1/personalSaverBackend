package com.vlad1m1.personal.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.OffsetDateTime;
import java.util.List;

@Schema(description = "Ответ для офлайн-синхронизации обновлений памяток.")
public record MemoUpdatesResponse(
        @Schema(description = "Время сервера, когда был сформирован ответ синхронизации.", example = "2026-05-12T10:15:30+03:00", requiredMode = Schema.RequiredMode.REQUIRED)
        OffsetDateTime generatedAt,

        @Schema(description = "Обновленные общие и региональные памятки, подходящие под параметры запроса.", requiredMode = Schema.RequiredMode.REQUIRED)
        List<MemoSummaryResponse> memos
) {
}
