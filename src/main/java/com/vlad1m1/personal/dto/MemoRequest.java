package com.vlad1m1.personal.dto;

import jakarta.validation.constraints.NotBlank;

import java.util.List;

public record MemoRequest(
        Long categoryId,
        Long regionId,
        @NotBlank(message = "title is required")
        String title,
        String shortDescription,
        String htmlContent,
        List<String> steps,
        Integer version,
        Boolean critical,
        String imageUrl,
        String iconName,
        String accentColor,
        Boolean active
) {
}
