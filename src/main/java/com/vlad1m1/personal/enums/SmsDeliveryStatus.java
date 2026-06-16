package com.vlad1m1.personal.enums;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
        description = "SMS delivery status returned for SOS diagnostics.",
        allowableValues = {"PENDING", "SENT", "FAILED"},
        example = "SENT"
)
public enum SmsDeliveryStatus {
    PENDING,
    SENT,
    FAILED
}
