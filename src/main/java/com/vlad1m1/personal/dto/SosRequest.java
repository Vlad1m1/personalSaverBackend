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

@Schema(name = "CreateSosRequest", description = "Request from the mobile app to create an SOS event and trigger SMS delivery.")
public record SosRequest(
        @Schema(description = "Delivery target. EMERGENCY_SERVICE routes to the regional emergency phone or 112. EMERGENCY_CONTACT routes to contactPhone from this request.", example = "EMERGENCY_CONTACT", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotNull(message = "targetType is required")
        SosTargetType targetType,

        @Schema(description = "Region id used for EMERGENCY_SERVICE routing and diagnostics. Optional for EMERGENCY_CONTACT.", example = "1", nullable = true)
        Long regionId,

        @Schema(description = "Latitude from the device. Strictly validated to the WGS84 range [-90, 90].", example = "55.755864", minimum = "-90", maximum = "90", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotNull(message = "latitude is required")
        @DecimalMin(value = "-90.0", message = "latitude must be greater than or equal to -90")
        @DecimalMax(value = "90.0", message = "latitude must be less than or equal to 90")
        Double latitude,

        @Schema(description = "Longitude from the device. Strictly validated to the WGS84 range [-180, 180].", example = "37.617698", minimum = "-180", maximum = "180", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotNull(message = "longitude is required")
        @DecimalMin(value = "-180.0", message = "longitude must be greater than or equal to -180")
        @DecimalMax(value = "180.0", message = "longitude must be less than or equal to 180")
        Double longitude,

        @Schema(description = "Device-reported location accuracy in meters.", example = "15", minimum = "0", maximum = "10000", nullable = true)
        @Max(value = 10000, message = "accuracyMeters must be less than or equal to 10000")
        Integer accuracyMeters,

        @Schema(description = "Human-readable address from the device geocoder.", example = "Moscow, Red Square", maxLength = 500, nullable = true)
        @Size(max = 500, message = "address must be at most 500 characters")
        String address,

        @Schema(description = "Emergency message. The backend stores exactly this text after trimming and does not infer user identity.", example = "I need help", minLength = 1, maxLength = 500, requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "message is required")
        @Size(max = 500, message = "message must be at most 500 characters")
        String message,

        @Schema(description = "Display name of the selected emergency contact. The backend does not store a contact list.", example = "Mom", maxLength = 120, nullable = true)
        @Size(max = 120, message = "contactName must be at most 120 characters")
        String contactName,

        @Schema(description = "Emergency contact phone for EMERGENCY_CONTACT. The backend normalizes it to a compact international-like format before routing.", example = "+79991234567", maxLength = 32, nullable = true)
        @Size(max = 32, message = "contactPhone must be at most 32 characters")
        @Pattern(regexp = "^$|^\\+?[0-9 ()-]{7,32}$", message = "contactPhone must contain a valid phone-like value")
        String contactPhone
) {
}
