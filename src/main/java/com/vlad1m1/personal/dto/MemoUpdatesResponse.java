package com.vlad1m1.personal.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.OffsetDateTime;
import java.util.List;

@Schema(name = "MemoUpdateResponse", description = "Ответ offline-синхронизации с памятками, измененными после указанного времени.")
public record MemoUpdatesResponse(
        @Schema(description = "Серверное время формирования ответа синхронизации.", example = "2026-05-12T10:15:30Z", requiredMode = Schema.RequiredMode.REQUIRED)
        OffsetDateTime generatedAt,

        @Schema(description = "Обновленные глобальные и региональные памятки, подходящие под since и regionId.", requiredMode = Schema.RequiredMode.REQUIRED)
        List<MemoSummaryResponse> memos
) {
}
