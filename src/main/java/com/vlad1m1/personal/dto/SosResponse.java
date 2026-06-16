package com.vlad1m1.personal.dto;

import com.vlad1m1.personal.enums.SosStatus;
import com.vlad1m1.personal.enums.SosTargetType;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.UUID;

@Schema(name = "SosResponse", description = "Созданное SOS-событие с маршрутизацией и диагностикой SMS-доставки.")
public record SosResponse(
        @Schema(description = "Идентификатор SOS-события для GET /api/sos/{id}.", example = "3b06b36f-8077-4f03-b8cf-bb7a7b9b6f6f", requiredMode = Schema.RequiredMode.REQUIRED)
        UUID id,

        @Schema(description = "Получатель доставки, выбранный мобильным приложением.", example = "EMERGENCY_CONTACT", requiredMode = Schema.RequiredMode.REQUIRED)
        SosTargetType targetType,

        @Schema(description = "Идентификатор региона из запроса, если он был передан.", example = "1", nullable = true)
        Long regionId,

        @Schema(description = "Текущий статус обработки SOS.", example = "SENT", requiredMode = Schema.RequiredMode.REQUIRED)
        SosStatus status,

        @Schema(description = "Телефон, выбранный backend после маршрутизации и нормализации.", example = "+79991234567", requiredMode = Schema.RequiredMode.REQUIRED)
        String targetPhone,

        @Schema(description = "Сохраненное экстренное сообщение.", example = "Нужна срочная помощь", requiredMode = Schema.RequiredMode.REQUIRED)
        String message,

        @Schema(description = "Сохраненная широта.", example = "55.755864", nullable = true)
        Double latitude,

        @Schema(description = "Сохраненная долгота.", example = "37.617698", nullable = true)
        Double longitude,

        @Schema(description = "Сохраненная точность устройства в метрах.", example = "15", nullable = true)
        Integer accuracyMeters,

        @Schema(description = "Сохраненный читаемый адрес.", example = "Москва, Красная площадь", nullable = true)
        String address,

        @Schema(description = "Диагностика SMS-доставки для Flutter-диагностики и логов.", requiredMode = Schema.RequiredMode.REQUIRED)
        SmsDeliveryResponse sms,

        @Schema(description = "Время создания SOS.", example = "2026-05-12T10:15:30", requiredMode = Schema.RequiredMode.REQUIRED)
        LocalDateTime createdAt,

        @Schema(description = "Время последнего обновления статуса.", example = "2026-05-12T10:15:31", requiredMode = Schema.RequiredMode.REQUIRED)
        LocalDateTime updatedAt
) {
}
