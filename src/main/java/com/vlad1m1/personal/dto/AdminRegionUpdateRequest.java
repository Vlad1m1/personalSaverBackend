package com.vlad1m1.personal.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(name = "AdminRegionUpdateRequest", description = "Admin request to update an existing region.")
public record AdminRegionUpdateRequest(
        @Schema(description = "Updated region name shown in the mobile app.", example = "Moscow", minLength = 1, maxLength = 120, requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "name is required")
        @Size(max = 120, message = "name must be at most 120 characters")
        String name,

        @Schema(description = "Updated regional emergency service phone. Null or blank means the default 112 is used by SOS.", example = "112", maxLength = 32, nullable = true)
        @Size(max = 32, message = "emergencyPhone must be at most 32 characters")
        String emergencyPhone
) {
}
