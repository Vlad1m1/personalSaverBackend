package com.vlad1m1.personal.dto;

import jakarta.validation.constraints.NotBlank;

public record MemoCategoryRequest(
        @NotBlank(message = "name is required")
        String name,
        String iconName,
        String accentColor,
        Integer displayOrder
) {
}
