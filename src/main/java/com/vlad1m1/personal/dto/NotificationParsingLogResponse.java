package com.vlad1m1.personal.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.OffsetDateTime;

@Schema(name = "NotificationParsingLogResponse", description = "Admin-visible result of a regional notification parsing run.")
public record NotificationParsingLogResponse(
        @Schema(description = "Log id. For the current in-memory parser it is the run timestamp.", example = "2026-05-12T10:00:00Z", requiredMode = Schema.RequiredMode.REQUIRED)
        String id,

        @Schema(description = "Parsing start time.", example = "2026-05-12T10:00:00Z", requiredMode = Schema.RequiredMode.REQUIRED)
        OffsetDateTime startedAt,

        @Schema(description = "Parsing finish time.", example = "2026-05-12T10:00:03Z", nullable = true)
        OffsetDateTime finishedAt,

        @Schema(description = "Run status.", example = "SUCCESS", allowableValues = {"SUCCESS", "FAILED"}, requiredMode = Schema.RequiredMode.REQUIRED)
        String status,

        @Schema(description = "Source that initiated parsing.", example = "manual", requiredMode = Schema.RequiredMode.REQUIRED)
        String source,

        @Schema(description = "Short operator-facing message.", example = "Manual notification parsing completed", requiredMode = Schema.RequiredMode.REQUIRED)
        String message,

        @Schema(description = "Number of created notifications.", example = "2", minimum = "0", requiredMode = Schema.RequiredMode.REQUIRED)
        int createdCount,

        @Schema(description = "Number of updated notifications.", example = "1", minimum = "0", requiredMode = Schema.RequiredMode.REQUIRED)
        int updatedCount,

        @Schema(description = "Error details if parsing failed.", example = "Source timeout", nullable = true)
        String errorDetails
) {
}
