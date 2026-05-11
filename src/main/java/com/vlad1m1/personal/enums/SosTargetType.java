package com.vlad1m1.personal.enums;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
        description = """
                Цель доставки SOS.
                EMERGENCY_SERVICE отправляет SMS на номер экстренной службы региона или на 112, если у региона нет emergencyPhone.
                EMERGENCY_CONTACT отправляет SMS на contactPhone, переданный в запросе.
                """,
        allowableValues = {"EMERGENCY_SERVICE", "EMERGENCY_CONTACT"},
        example = "EMERGENCY_CONTACT"
)
public enum SosTargetType {
    EMERGENCY_SERVICE,
    EMERGENCY_CONTACT
}
