package com.vlad1m1.personal.enums;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
        description = "SOS delivery target. EMERGENCY_SERVICE sends to the region emergency phone or 112. EMERGENCY_CONTACT sends to contactPhone from the request.",
        allowableValues = {"EMERGENCY_SERVICE", "EMERGENCY_CONTACT"},
        example = "EMERGENCY_CONTACT"
)
public enum SosTargetType {
    EMERGENCY_SERVICE,
    EMERGENCY_CONTACT
}
