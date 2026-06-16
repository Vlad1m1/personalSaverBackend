package com.vlad1m1.personal.dto;

import com.vlad1m1.personal.enums.SosStatus;
import com.vlad1m1.personal.enums.SosTargetType;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.UUID;

@Schema(name = "SosResponse", description = "Created SOS event with routing and SMS delivery diagnostics.")
public record SosResponse(
        @Schema(description = "SOS event id for GET /api/sos/{id}.", example = "3b06b36f-8077-4f03-b8cf-bb7a7b9b6f6f", requiredMode = Schema.RequiredMode.REQUIRED)
        UUID id,

        @Schema(description = "Delivery target selected by the mobile app.", example = "EMERGENCY_CONTACT", requiredMode = Schema.RequiredMode.REQUIRED)
        SosTargetType targetType,

        @Schema(description = "Region id from the request, if one was provided.", example = "1", nullable = true)
        Long regionId,

        @Schema(description = "Current SOS processing status.", example = "SENT", requiredMode = Schema.RequiredMode.REQUIRED)
        SosStatus status,

        @Schema(description = "Phone number selected by the backend after routing and normalization.", example = "+79991234567", requiredMode = Schema.RequiredMode.REQUIRED)
        String targetPhone,

        @Schema(description = "Stored emergency message.", example = "I need help", requiredMode = Schema.RequiredMode.REQUIRED)
        String message,

        @Schema(description = "Stored latitude.", example = "55.755864", nullable = true)
        Double latitude,

        @Schema(description = "Stored longitude.", example = "37.617698", nullable = true)
        Double longitude,

        @Schema(description = "Stored device accuracy in meters.", example = "15", nullable = true)
        Integer accuracyMeters,

        @Schema(description = "Stored human-readable address.", example = "Moscow, Red Square", nullable = true)
        String address,

        @Schema(description = "SMS delivery diagnostics for Flutter diagnostics and logs.", requiredMode = Schema.RequiredMode.REQUIRED)
        SmsDeliveryResponse sms,

        @Schema(description = "SOS creation timestamp.", example = "2026-05-12T10:15:30", requiredMode = Schema.RequiredMode.REQUIRED)
        LocalDateTime createdAt,

        @Schema(description = "Last status update timestamp.", example = "2026-05-12T10:15:31", requiredMode = Schema.RequiredMode.REQUIRED)
        LocalDateTime updatedAt
) {
}
