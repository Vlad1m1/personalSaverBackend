package com.vlad1m1.personal.controller;

import com.vlad1m1.personal.config.OpenApiExamples;
import com.vlad1m1.personal.dto.ApiErrorResponse;
import com.vlad1m1.personal.dto.SosRequest;
import com.vlad1m1.personal.dto.SosResponse;
import com.vlad1m1.personal.service.SosService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/sos")
@Tag(name = "Public SOS")
public class SosController {

    private final SosService sosService;

    public SosController(SosService sosService) {
        this.sosService = sosService;
    }

    @Operation(
            summary = "Create SOS event",
            description = """
                    Creates an SOS event from the Flutter app and starts SMS delivery.

                    Default rate limit: 5 POST /api/sos requests per minute from one IP address. Exceeding it returns 429 Too Many Requests.

                    Coordinates are strictly validated: latitude must be between -90 and 90, longitude between -180 and 180.
                    message is required and limited to 500 characters. contactPhone is normalized before routing.

                    The backend does not know the current app user and does not store the user's contact list. For EMERGENCY_CONTACT,
                    the selected contact phone is sent in this request only. For EMERGENCY_SERVICE, the backend uses the region emergency phone
                    or falls back to 112.
                    """
    )
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "SOS payload produced by the mobile app.",
            required = true,
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = SosRequest.class),
                    examples = {
                            @ExampleObject(name = "SOS to emergency contact", value = OpenApiExamples.SOS_CONTACT_REQUEST),
                            @ExampleObject(name = "SOS to emergency service", value = OpenApiExamples.SOS_SERVICE_REQUEST)
                    })
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "SOS event created and SMS accepted for delivery",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = SosResponse.class),
                            examples = @ExampleObject(name = "Created SOS event", value = OpenApiExamples.SOS_RESPONSE))),
            @ApiResponse(responseCode = "400", description = "Validation Error",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ApiErrorResponse.class),
                            examples = @ExampleObject(name = "Validation Error", value = OpenApiExamples.VALIDATION_ERROR))),
            @ApiResponse(responseCode = "404", description = "Region not found",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ApiErrorResponse.class),
                            examples = @ExampleObject(name = "Not Found", value = OpenApiExamples.NOT_FOUND_ERROR))),
            @ApiResponse(responseCode = "429", description = "Too Many Requests",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ApiErrorResponse.class),
                            examples = @ExampleObject(name = "Rate limit", value = OpenApiExamples.RATE_LIMIT_ERROR))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ApiErrorResponse.class),
                            examples = @ExampleObject(name = "Internal Server Error", value = OpenApiExamples.INTERNAL_ERROR)))
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SosResponse createSos(
            @org.springframework.web.bind.annotation.RequestBody @Valid SosRequest request
    ) {
        return sosService.create(request);
    }

    @Operation(summary = "Get SOS diagnostics", description = "Diagnostic endpoint for a specific SOS event. Use it when the mobile app needs to show or log final SOS/SMS delivery state.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "SOS event returned",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = SosResponse.class),
                            examples = @ExampleObject(name = "SOS result", value = OpenApiExamples.SOS_RESPONSE))),
            @ApiResponse(responseCode = "404", description = "SOS event not found",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ApiErrorResponse.class),
                            examples = @ExampleObject(name = "Not Found", value = OpenApiExamples.NOT_FOUND_ERROR))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ApiErrorResponse.class),
                            examples = @ExampleObject(name = "Internal Server Error", value = OpenApiExamples.INTERNAL_ERROR)))
    })
    @GetMapping("/{id}")
    public SosResponse getSosById(
            @Parameter(description = "SOS event id returned by POST /api/sos.", example = "3b06b36f-8077-4f03-b8cf-bb7a7b9b6f6f", required = true)
            @PathVariable UUID id
    ) {
        return sosService.getById(id);
    }
}
