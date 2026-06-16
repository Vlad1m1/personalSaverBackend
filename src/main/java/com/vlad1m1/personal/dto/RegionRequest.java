package com.vlad1m1.personal.dto;

import jakarta.validation.constraints.NotBlank;

public record RegionRequest(
        @NotBlank(message = "name is required")
        String name,
        String emergencyPhone
) {
}
