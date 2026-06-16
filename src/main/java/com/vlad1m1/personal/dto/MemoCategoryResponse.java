package com.vlad1m1.personal.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(description = "Category for grouping memo content in the Flutter app.")
public record MemoCategoryResponse(
        @Schema(description = "Category id.", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
        Long id,

        @Schema(description = "Category display name.", example = "First Aid", requiredMode = Schema.RequiredMode.REQUIRED)
        String name,

        @Schema(description = "Flutter icon key or design-system icon name.", example = "medical_services", nullable = true)
        String iconName,

        @Schema(description = "HEX accent color.", example = "#D32F2F", nullable = true)
        String accentColor,

        @Schema(description = "Sort order. Lower values are displayed first.", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
        int displayOrder,

        @Schema(description = "Last category update timestamp for cache invalidation.", example = "2026-05-12T08:30:00", nullable = true)
        LocalDateTime updatedAt
) {
}
