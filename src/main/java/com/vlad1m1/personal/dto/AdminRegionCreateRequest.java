package com.vlad1m1.personal.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(name = "AdminRegionCreateRequest", description = "Административный запрос на создание региона для памяток, уведомлений и SOS-маршрутизации.")
public record AdminRegionCreateRequest(
        @Schema(description = "Название региона, отображаемое в мобильном приложении.", example = "Москва", minLength = 1, maxLength = 120, requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "name is required")
        @Size(max = 120, message = "name must be at most 120 characters")
        String name,

        @Schema(description = "Телефон экстренной службы региона. Если не передан, SOS использует резервный номер 112.", example = "112", maxLength = 32, nullable = true)
        @Size(max = 32, message = "emergencyPhone must be at most 32 characters")
        String emergencyPhone
) {
}
