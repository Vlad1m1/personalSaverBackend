package com.vlad1m1.personal.controller;

import com.vlad1m1.personal.config.OpenApiExamples;
import com.vlad1m1.personal.dto.ApiErrorResponse;
import com.vlad1m1.personal.dto.SmsDeliveryResponse;
import com.vlad1m1.personal.service.SmsService;
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
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/sms")
@Tag(name = "Public SOS")
public class SmsController {
    private static final String SMS_RESPONSE = """
            {
              "recipientPhone": "+79991234567",
              "status": "SENT",
              "providerMessage": "Accepted by SMS provider"
            }
            """;

    private final SmsService smsService;

    public SmsController(SmsService smsService) {
        this.smsService = smsService;
    }

    @Operation(summary = "Get SOS SMS delivery status", description = "Diagnostic endpoint for SMS delivery related to an SOS event. The mobile app does not send arbitrary SMS through this API; SMS delivery is created by POST /api/sos.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "SMS delivery status returned",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = SmsDeliveryResponse.class),
                            examples = @ExampleObject(name = "SMS delivery", value = SMS_RESPONSE))),
            @ApiResponse(responseCode = "404", description = "SOS event not found",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ApiErrorResponse.class),
                            examples = @ExampleObject(name = "Not Found", value = OpenApiExamples.NOT_FOUND_ERROR))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ApiErrorResponse.class),
                            examples = @ExampleObject(name = "Internal Server Error", value = OpenApiExamples.INTERNAL_ERROR)))
    })
    @GetMapping("/sos/{sosId}")
    public SmsDeliveryResponse getSosSmsDelivery(
            @Parameter(description = "SOS event id returned by POST /api/sos.", example = "3b06b36f-8077-4f03-b8cf-bb7a7b9b6f6f", required = true)
            @PathVariable UUID sosId
    ) {
        return smsService.getSosSmsDelivery(sosId);
    }
}
