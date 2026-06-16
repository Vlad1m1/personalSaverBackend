package com.vlad1m1.personal.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.OffsetDateTime;
import java.util.List;

@Schema(name = "MemoUpdateResponse", description = "Offline synchronization response with memos changed after the requested timestamp.")
public record MemoUpdatesResponse(
        @Schema(description = "Server time when the sync response was generated.", example = "2026-05-12T10:15:30Z", requiredMode = Schema.RequiredMode.REQUIRED)
        OffsetDateTime generatedAt,

        @Schema(description = "Updated global and regional memos matching since and regionId.", requiredMode = Schema.RequiredMode.REQUIRED)
        List<MemoSummaryResponse> memos
) {
}
