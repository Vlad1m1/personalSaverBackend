package com.vlad1m1.personal.enums;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
        description = "Текущее состояние обработки SOS-события.",
        allowableValues = {"CREATED", "SENDING", "SENT", "FAILED"},
        example = "SENT"
)
public enum SosStatus {
    CREATED,
    SENDING,
    SENT,
    FAILED
}
