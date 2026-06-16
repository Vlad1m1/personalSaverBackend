package com.vlad1m1.personal.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.List;

@Schema(name = "AdminMemoCreateRequest", description = "Admin request to create a memo. New memos may be kept unpublished with active=false.")
public record AdminMemoCreateRequest(
        @Schema(description = "Memo category id.", example = "1", nullable = true)
        Long categoryId,

        @Schema(description = "Region id for regional memo. Null means the memo is global.", example = "1", nullable = true)
        Long regionId,

        @Schema(description = "Memo title shown in list and details screens.", example = "What to do during a fire", minLength = 1, maxLength = 160, requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "title is required")
        @Size(max = 160, message = "title must be at most 160 characters")
        String title,

        @Schema(description = "Short list-screen summary. htmlContent is not returned by GET /api/memos.", example = "Short fire safety instruction", maxLength = 500, nullable = true)
        @Size(max = 500, message = "shortDescription must be at most 500 characters")
        String shortDescription,

        @Schema(description = "HTML body for Flutter WebView on the memo details screen.", example = "<h1>What to do during a fire</h1><ol><li>Leave the building.</li><li>Call 112.</li></ol>", nullable = true)
        String htmlContent,

        @Schema(description = "Optional structured steps for native rendering.", nullable = true)
        List<String> steps,

        @Schema(description = "Content version used by offline synchronization.", example = "3", minimum = "1", nullable = true)
        Integer version,

        @Schema(description = "Marks high-priority safety content.", example = "true", nullable = true)
        Boolean critical,

        @Schema(description = "Optional image URL displayed by the mobile app.", example = "https://cdn.example.com/memos/fire.png", nullable = true)
        String imageUrl,

        @Schema(description = "Optional Flutter icon key.", example = "local_fire_department", nullable = true)
        String iconName,

        @Schema(description = "Optional HEX accent color.", example = "#D32F2F", nullable = true)
        String accentColor,

        @Schema(description = "Publication flag. false creates a draft; publish endpoint sets this to true.", example = "false", nullable = true)
        Boolean active
) {
}
