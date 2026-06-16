package com.vlad1m1.personal.dto;

import com.vlad1m1.personal.enums.SmsDeliveryStatus;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "SMS delivery diagnostics for an SOS event.")
public record SmsDeliveryResponse(
        @Schema(description = "Recipient phone selected by the backend routing rules.", example = "+79991234567", requiredMode = Schema.RequiredMode.REQUIRED)
        String recipientPhone,

        @Schema(description = "SMS provider or simulator delivery status.", example = "SENT", requiredMode = Schema.RequiredMode.REQUIRED)
        SmsDeliveryStatus status,

        @Schema(description = "Optional provider message or diagnostic error.", example = "Accepted by SMS provider", nullable = true)
        String providerMessage
) {
}
