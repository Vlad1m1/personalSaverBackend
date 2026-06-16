package com.vlad1m1.personal.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Region available in the mobile application.")
public record RegionResponse(
        @Schema(description = "Region id used by memos, notifications, and SOS endpoints.", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
        Long id,

        @Schema(description = "Region display name.", example = "Moscow", requiredMode = Schema.RequiredMode.REQUIRED)
        String name,

        @Schema(description = "Regional emergency service phone. If null, SOS uses 112.", example = "112", nullable = true)
        String emergencyPhone
) {
}
