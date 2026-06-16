package com.vlad1m1.personal.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(name = "AdminRegionUpdateRequest", description = "Административный запрос на обновление существующего региона.")
public record AdminRegionUpdateRequest(
        @Schema(description = "Обновленное название региона для мобильного приложения.", example = "Москва", minLength = 1, maxLength = 120, requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "name is required")
        @Size(max = 120, message = "name must be at most 120 characters")
        String name,

        @Schema(description = "Обновленный телефон экстренной службы региона. Null или пустое значение означает резервный номер 112 для SOS.", example = "112", maxLength = 32, nullable = true)
        @Size(max = 32, message = "emergencyPhone must be at most 32 characters")
        String emergencyPhone
) {
}
