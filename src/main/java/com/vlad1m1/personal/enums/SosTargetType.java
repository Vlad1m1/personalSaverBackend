package com.vlad1m1.personal.enums;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
        description = "Получатель SOS-доставки. EMERGENCY_SERVICE отправляет на телефон экстренной службы региона или 112. EMERGENCY_CONTACT отправляет на contactPhone из запроса.",
        allowableValues = {"EMERGENCY_SERVICE", "EMERGENCY_CONTACT"},
        example = "EMERGENCY_CONTACT"
)
public enum SosTargetType {
    EMERGENCY_SERVICE,
    EMERGENCY_CONTACT
}
