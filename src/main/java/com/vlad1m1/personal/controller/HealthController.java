package com.vlad1m1.personal.controller;

import com.vlad1m1.personal.config.OpenApiExamples;
import com.vlad1m1.personal.dto.ApiErrorResponse;
import com.vlad1m1.personal.dto.HealthResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.OffsetDateTime;

@RestController
@RequestMapping("/api/health")
@Tag(name = "Health")
public class HealthController {

    @Operation(summary = "Check backend health", description = "Simple smoke-check endpoint for mobile developers, CI, and deployment diagnostics.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Service is available",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = HealthResponse.class),
                            examples = @ExampleObject(name = "Service status", value = OpenApiExamples.HEALTH_RESPONSE))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ApiErrorResponse.class),
                            examples = @ExampleObject(name = "Internal Server Error", value = OpenApiExamples.INTERNAL_ERROR)))
    })
    @GetMapping
    public HealthResponse getHealth() {
        return new HealthResponse("UP", "personal-rescue-backend", OffsetDateTime.now());
    }
}
