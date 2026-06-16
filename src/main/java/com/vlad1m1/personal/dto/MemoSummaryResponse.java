package com.vlad1m1.personal.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.UUID;

@Schema(name = "MemoListResponse", description = "Memo item for catalog and update lists. htmlContent is intentionally omitted.")
public record MemoSummaryResponse(
        @Schema(description = "Memo id used for GET /api/memos/{id}.", example = "8c7a15f8-2f68-4d8d-8cbb-2b26f1c4b4d3", requiredMode = Schema.RequiredMode.REQUIRED)
        UUID id,

        @Schema(description = "Category id used for grouping.", example = "1", nullable = true)
        Long categoryId,

        @Schema(description = "Region id. Null means global memo available in all regions.", example = "1", nullable = true)
        Long regionId,

        @Schema(description = "Memo title.", example = "What to do during a fire", requiredMode = Schema.RequiredMode.REQUIRED)
        String title,

        @Schema(description = "Stable slug generated from title for client cache keys and analytics.", example = "what-to-do-during-a-fire", requiredMode = Schema.RequiredMode.REQUIRED)
        String slug,

        @Schema(description = "Short catalog summary.", example = "Short fire safety instruction", nullable = true)
        String shortDescription,

        @Schema(description = "Hash of memo body and metadata. Use it to decide whether cached content changed.", example = "sha256:4b4f3c0d7f9a", requiredMode = Schema.RequiredMode.REQUIRED)
        String contentHash,

        @Schema(description = "Monotonic content version for offline synchronization.", example = "3", requiredMode = Schema.RequiredMode.REQUIRED)
        int version,

        @Schema(description = "Last update timestamp used by GET /api/memos/updates?since=...", example = "2026-05-12T10:00:00", requiredMode = Schema.RequiredMode.REQUIRED)
        LocalDateTime updatedAt
) {
}
