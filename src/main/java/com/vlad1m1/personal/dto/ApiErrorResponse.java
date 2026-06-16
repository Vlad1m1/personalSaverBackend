package com.vlad1m1.personal.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.OffsetDateTime;
import java.util.List;

@Schema(name = "ApiErrorResponse", description = "Unified error response returned by public and admin API endpoints.")
public record ApiErrorResponse(
        @Schema(description = "UTC or server-offset timestamp when the error response was produced.", example = "2026-05-12T10:00:00Z", requiredMode = Schema.RequiredMode.REQUIRED)
        OffsetDateTime timestamp,

        @Schema(description = "HTTP status code.", example = "400", minimum = "100", maximum = "599", requiredMode = Schema.RequiredMode.REQUIRED)
        int status,

        @Schema(description = "HTTP reason phrase.", example = "Bad Request", requiredMode = Schema.RequiredMode.REQUIRED)
        String error,

        @Schema(description = "Human-readable error message for logs and developer tooling.", example = "Validation error", requiredMode = Schema.RequiredMode.REQUIRED)
        String message,

        @Schema(description = "Request path where the error happened.", example = "/api/sos", requiredMode = Schema.RequiredMode.REQUIRED)
        String path,

        @Schema(description = "Request correlation id. If X-Request-Id was sent, the same value is returned.", example = "7f3c2d8a", requiredMode = Schema.RequiredMode.REQUIRED)
        String requestId,

        @Schema(description = "Field-level or domain-specific error details. Empty array means no structured details.", requiredMode = Schema.RequiredMode.REQUIRED)
        List<ApiErrorDetail> details
) {
    @Schema(name = "ApiErrorDetail", description = "Single validation or domain error detail.")
    public record ApiErrorDetail(
            @Schema(description = "Field, parameter, or domain object that caused the error.", example = "latitude", nullable = true)
            String field,

            @Schema(description = "Developer-facing detail message.", example = "must be greater than or equal to -90", requiredMode = Schema.RequiredMode.REQUIRED)
            String message
    ) {
    }
}
