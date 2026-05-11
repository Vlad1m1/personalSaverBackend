package com.vlad1m1.personal.dto;

import com.vlad1m1.personal.enums.SosTargetType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Запрос на создание SOS-события и отправку SMS.")
public record SosRequest(
        @Schema(description = "Режим доставки. EMERGENCY_SERVICE игнорирует contactPhone. EMERGENCY_CONTACT требует contactPhone.", example = "EMERGENCY_CONTACT", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotNull(message = "targetType обязателен")
        SosTargetType targetType,

        @Schema(description = "Регион для выбора emergencyPhone в режиме EMERGENCY_SERVICE и для диагностики.", example = "1")
        Long regionId,

        @Schema(description = "Телефон выбранного пользователем экстренного контакта. Обязателен только для EMERGENCY_CONTACT и игнорируется для EMERGENCY_SERVICE.", example = "+79991234567")
        String contactPhone,

        @Schema(description = "Экстренное сообщение, сформированное мобильным приложением.", example = "Мне нужна помощь. Моя геопозиция приложена.", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "message обязателен")
        String message,

        @Schema(description = "Широта с мобильного устройства, если пользователь выдал разрешение на геопозицию.", example = "55.7558")
        Double latitude,

        @Schema(description = "Долгота с мобильного устройства, если пользователь выдал разрешение на геопозицию.", example = "37.6173")
        Double longitude
) {
}
