package com.vlad1m1.personal.dto;

import com.vlad1m1.personal.enums.SosTargetType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Schema(name = "CreateSosRequest", description = "Запрос мобильного приложения на создание SOS-события и запуск SMS-доставки.")
public record SosRequest(
        @Schema(description = "Получатель доставки. EMERGENCY_SERVICE отправляет на телефон экстренной службы региона или 112. EMERGENCY_CONTACT отправляет на contactPhone из запроса.", example = "EMERGENCY_CONTACT", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotNull(message = "targetType is required")
        SosTargetType targetType,

        @Schema(description = "Идентификатор региона для маршрутизации EMERGENCY_SERVICE и диагностики. Для EMERGENCY_CONTACT необязателен.", example = "1", nullable = true)
        Long regionId,

        @Schema(description = "Широта с устройства. Если передана, валидируется в диапазоне WGS84 [-90, 90].", example = "55.755864", minimum = "-90", maximum = "90", nullable = true)
        @DecimalMin(value = "-90.0", message = "latitude must be greater than or equal to -90")
        @DecimalMax(value = "90.0", message = "latitude must be less than or equal to 90")
        Double latitude,

        @Schema(description = "Долгота с устройства. Если передана, валидируется в диапазоне WGS84 [-180, 180].", example = "37.617698", minimum = "-180", maximum = "180", nullable = true)
        @DecimalMin(value = "-180.0", message = "longitude must be greater than or equal to -180")
        @DecimalMax(value = "180.0", message = "longitude must be less than or equal to 180")
        Double longitude,

        @Schema(description = "Точность геолокации в метрах по данным устройства.", example = "15", minimum = "0", maximum = "10000", nullable = true)
        @Max(value = 10000, message = "accuracyMeters must be less than or equal to 10000")
        Integer accuracyMeters,

        @Schema(description = "Читаемый адрес от геокодера устройства.", example = "Москва, Красная площадь", maxLength = 500, nullable = true)
        @Size(max = 500, message = "address must be at most 500 characters")
        String address,

        @Schema(description = "Экстренное сообщение. Backend сохраняет этот текст после trim и не пытается определить личность пользователя.", example = "Нужна срочная помощь", minLength = 1, maxLength = 500, requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "message is required")
        @Size(max = 500, message = "message must be at most 500 characters")
        String message,

        @Schema(description = "Отображаемое имя выбранного экстренного контакта. Backend не хранит список контактов.", example = "Мама", maxLength = 120, nullable = true)
        @Size(max = 120, message = "contactName must be at most 120 characters")
        String contactName,

        @Schema(description = "Телефон экстренного контакта для EMERGENCY_CONTACT. Backend нормализует его перед маршрутизацией.", example = "+79991234567", maxLength = 32, nullable = true)
        @Size(max = 32, message = "contactPhone must be at most 32 characters")
        @Pattern(regexp = "^$|^\\+?[0-9 ()-]{7,32}$", message = "contactPhone must contain a valid phone-like value")
        String contactPhone
) {
}
