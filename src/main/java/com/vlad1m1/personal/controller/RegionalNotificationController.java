package com.vlad1m1.personal.controller;

import com.vlad1m1.personal.config.OpenApiExamples;
import com.vlad1m1.personal.dto.ApiErrorResponse;
import com.vlad1m1.personal.dto.RegionalNotificationResponse;
import com.vlad1m1.personal.enums.NotificationSeverity;
import com.vlad1m1.personal.service.RegionalNotificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/notifications")
@Tag(name = "Public Regional Notifications")
public class RegionalNotificationController {
    private final RegionalNotificationService regionalNotificationService;

    public RegionalNotificationController(RegionalNotificationService regionalNotificationService) {
        this.regionalNotificationService = regionalNotificationService;
    }

    @Operation(summary = "List regional notifications", description = "Returns active notifications for a region. Use severity to filter WARNING or CRITICAL views. Results are sorted by publishedAt descending, with receivedAt as fallback metadata.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Notifications returned",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = RegionalNotificationResponse.class),
                            examples = {
                                    @ExampleObject(name = "severity=WARNING", value = OpenApiExamples.NOTIFICATIONS_WARNING_RESPONSE),
                                    @ExampleObject(name = "severity=CRITICAL", value = OpenApiExamples.NOTIFICATIONS_CRITICAL_RESPONSE)
                            })),
            @ApiResponse(responseCode = "400", description = "Invalid query parameter",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class), examples = @ExampleObject(value = OpenApiExamples.VALIDATION_ERROR))),
            @ApiResponse(responseCode = "404", description = "Region not found",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class), examples = @ExampleObject(value = OpenApiExamples.NOT_FOUND_ERROR))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class), examples = @ExampleObject(value = OpenApiExamples.INTERNAL_ERROR)))
    })
    @GetMapping
    public List<RegionalNotificationResponse> getNotifications(
            @Parameter(description = "Required region id.", example = "1", required = true)
            @RequestParam Long regionId,
            @Parameter(description = "Optional severity filter.", example = "WARNING")
            @RequestParam(required = false) NotificationSeverity severity,
            @Parameter(description = "Maximum number of items. Default 50, maximum 100.", example = "20")
            @RequestParam(required = false) Integer limit
    ) {
        return regionalNotificationService.getByRegion(regionId, severity, limit);
    }

    @Operation(summary = "Get latest regional notifications", description = "Returns the latest active notifications for the app home screen. Use this endpoint for compact top-of-screen warning widgets.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Latest notifications returned",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = RegionalNotificationResponse.class),
                            examples = @ExampleObject(name = "Latest notifications", value = OpenApiExamples.NOTIFICATIONS_WARNING_RESPONSE))),
            @ApiResponse(responseCode = "404", description = "Region not found",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class), examples = @ExampleObject(value = OpenApiExamples.NOT_FOUND_ERROR))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class), examples = @ExampleObject(value = OpenApiExamples.INTERNAL_ERROR)))
    })
    @GetMapping("/latest")
    public List<RegionalNotificationResponse> getLatestNotifications(
            @Parameter(description = "Required region id.", example = "1", required = true)
            @RequestParam Long regionId
    ) {
        return regionalNotificationService.getLatestByRegion(regionId);
    }

    @Operation(summary = "Get regional notification", description = "Diagnostic endpoint for opening or logging a specific notification by id.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Notification returned",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = RegionalNotificationResponse.class),
                            examples = @ExampleObject(name = "Notification", value = OpenApiExamples.NOTIFICATION_RESPONSE))),
            @ApiResponse(responseCode = "404", description = "Notification not found",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class), examples = @ExampleObject(value = OpenApiExamples.NOT_FOUND_ERROR))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class), examples = @ExampleObject(value = OpenApiExamples.INTERNAL_ERROR)))
    })
    @GetMapping("/{id}")
    public RegionalNotificationResponse getNotificationById(
            @Parameter(description = "Notification id.", example = "d8df5b89-6845-4b2f-ae58-b3c6c8edc7fb", required = true)
            @PathVariable UUID id
    ) {
        return regionalNotificationService.getById(id);
    }
}
