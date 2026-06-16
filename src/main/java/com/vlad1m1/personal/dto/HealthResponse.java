package com.vlad1m1.personal.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.OffsetDateTime;

@Schema(description = "Basic service status for smoke checks.")
public record HealthResponse(
        @Schema(description = "Current service status.", example = "UP", requiredMode = Schema.RequiredMode.REQUIRED)
        String status,

        @Schema(description = "Service name.", example = "personal-rescue-backend", requiredMode = Schema.RequiredMode.REQUIRED)
        String service,

        @Schema(description = "Server timestamp when the response was generated.", example = "2026-05-12T10:15:30Z", requiredMode = Schema.RequiredMode.REQUIRED)
        OffsetDateTime timestamp
) {
}
