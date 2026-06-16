package com.vlad1m1.personal.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.List;

@Schema(name = "AdminMemoUpdateRequest", description = "Admin request to replace editable memo fields.")
public record AdminMemoUpdateRequest(
        @Schema(description = "Updated memo category id.", example = "1", nullable = true)
        Long categoryId,

        @Schema(description = "Updated region id. Null means the memo is global.", example = "1", nullable = true)
        Long regionId,

        @Schema(description = "Updated memo title.", example = "What to do during a fire", minLength = 1, maxLength = 160, requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "title is required")
        @Size(max = 160, message = "title must be at most 160 characters")
        String title,

        @Schema(description = "Updated short list-screen summary.", example = "Short fire safety instruction", maxLength = 500, nullable = true)
        @Size(max = 500, message = "shortDescription must be at most 500 characters")
        String shortDescription,

        @Schema(description = "Updated HTML body for Flutter WebView.", example = "<h1>What to do during a fire</h1><ol><li>Leave the building.</li><li>Call 112.</li></ol>", nullable = true)
        String htmlContent,

        @Schema(description = "Updated optional native steps.", nullable = true)
        List<String> steps,

        @Schema(description = "Updated content version used by offline synchronization.", example = "4", minimum = "1", nullable = true)
        Integer version,

        @Schema(description = "Updated high-priority flag.", example = "true", nullable = true)
        Boolean critical,

        @Schema(description = "Updated optional image URL.", example = "https://cdn.example.com/memos/fire.png", nullable = true)
        String imageUrl,

        @Schema(description = "Updated optional Flutter icon key.", example = "local_fire_department", nullable = true)
        String iconName,

        @Schema(description = "Updated optional HEX accent color.", example = "#D32F2F", nullable = true)
        String accentColor,

        @Schema(description = "Publication flag. Use publish/unpublish endpoints for explicit workflow transitions.", example = "true", nullable = true)
        Boolean active
) {
}
