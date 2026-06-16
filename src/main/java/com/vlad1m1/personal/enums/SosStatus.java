package com.vlad1m1.personal.enums;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
        description = "Current SOS processing state.",
        allowableValues = {"CREATED", "SENDING", "SENT", "FAILED"},
        example = "SENT"
)
public enum SosStatus {
    CREATED,
    SENDING,
    SENT,
    FAILED
}
