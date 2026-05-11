package com.vlad1m1.personal.enums;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
        description = "Статус SMS-доставки, который мобильное приложение использует для диагностики.",
        allowableValues = {"PENDING", "SENT", "FAILED"},
        example = "SENT"
)
public enum SmsDeliveryStatus {
    PENDING,
    SENT,
    FAILED
}
