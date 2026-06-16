package com.vlad1m1.personal.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Schema(name = "MemoDetailsResponse", description = "Full memo payload for details screens. Includes htmlContent for Flutter WebView.")
public record MemoDetailResponse(
        @Schema(description = "Memo id.", example = "8c7a15f8-2f68-4d8d-8cbb-2b26f1c4b4d3", requiredMode = Schema.RequiredMode.REQUIRED)
        UUID id,

        @Schema(description = "Category id used for grouping.", example = "1", nullable = true)
        Long categoryId,

        @Schema(description = "Region id. Null means global memo.", example = "1", nullable = true)
        Long regionId,

        @Schema(description = "Memo title.", example = "What to do during a fire", requiredMode = Schema.RequiredMode.REQUIRED)
        String title,

        @Schema(description = "Stable slug generated from title.", example = "what-to-do-during-a-fire", requiredMode = Schema.RequiredMode.REQUIRED)
        String slug,

        @Schema(description = "Short details summary.", example = "Short fire safety instruction", nullable = true)
        String shortDescription,

        @Schema(description = "Trusted HTML content rendered by Flutter WebView.", example = "<h1>What to do during a fire</h1><ol><li>Leave the building.</li><li>Call 112.</li></ol>", nullable = true)
        String htmlContent,

        @Schema(description = "Optional native steps for clients that do not render HTML.", nullable = true)
        List<String> steps,

        @Schema(description = "Flutter icon key.", example = "local_fire_department", nullable = true)
        String iconName,

        @Schema(description = "HEX accent color.", example = "#D32F2F", nullable = true)
        String accentColor,

        @Schema(description = "Hash of memo content and metadata.", example = "sha256:4b4f3c0d7f9a", requiredMode = Schema.RequiredMode.REQUIRED)
        String contentHash,

        @Schema(description = "Content version for offline synchronization.", example = "3", requiredMode = Schema.RequiredMode.REQUIRED)
        int version,

        @Schema(description = "High-priority content flag.", example = "true", requiredMode = Schema.RequiredMode.REQUIRED)
        boolean critical,

        @Schema(description = "Optional image URL.", example = "https://cdn.example.com/memos/fire.png", nullable = true)
        String imageUrl,

        @Schema(description = "Last update timestamp. ETag and Last-Modified headers are not currently implemented; use contentHash/version/updatedAt instead.", example = "2026-05-12T10:00:00", requiredMode = Schema.RequiredMode.REQUIRED)
        LocalDateTime updatedAt
) {
}
