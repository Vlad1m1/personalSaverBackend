package com.vlad1m1.personal.dto;

import com.vlad1m1.personal.enums.SmsDeliveryStatus;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Диагностика SMS-доставки для SOS-события.")
public record SmsDeliveryResponse(
        @Schema(description = "Телефон получателя, выбранный backend по правилам маршрутизации.", example = "+79991234567", requiredMode = Schema.RequiredMode.REQUIRED)
        String recipientPhone,

        @Schema(description = "Статус доставки от SMS-провайдера или симулятора.", example = "SENT", requiredMode = Schema.RequiredMode.REQUIRED)
        SmsDeliveryStatus status,

        @Schema(description = "Необязательное сообщение провайдера или диагностическая ошибка.", example = "Accepted by SMS provider", nullable = true)
        String providerMessage
) {
}
