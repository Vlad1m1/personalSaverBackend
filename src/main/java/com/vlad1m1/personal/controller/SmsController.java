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
@Tag(name = "SMS")
public class SmsController {
    private static final String SMS_RESPONSE = """
            {
              "recipientPhone": "+79991234567",
              "status": "SENT",
              "providerMessage": "Принято SMS-провайдером"
            }
            """;

    private final SmsService smsService;

    public SmsController(SmsService smsService) {
        this.smsService = smsService;
    }

    @Operation(
            summary = "Получить статус SMS-доставки для SOS",
            description = """
                    Диагностический endpoint для статуса SMS-доставки, связанной с SOS-событием.
                    Мобильное приложение не отправляет произвольные SMS через этот API; SMS-доставка создается через POST /api/sos.
                    """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Статус SMS-доставки успешно возвращен",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = SmsDeliveryResponse.class),
                            examples = @ExampleObject(name = "SMS-доставка", value = SMS_RESPONSE))),
            @ApiResponse(responseCode = "404", description = "SOS-событие не найдено",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ApiErrorResponse.class),
                            examples = @ExampleObject(name = "Не найдено", value = OpenApiExamples.NOT_FOUND_ERROR))),
            @ApiResponse(responseCode = "500", description = "Непредвиденная ошибка сервера",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ApiErrorResponse.class),
                            examples = @ExampleObject(name = "Ошибка сервера", value = OpenApiExamples.INTERNAL_ERROR)))
    })
    @GetMapping("/sos/{sosId}")
    public SmsDeliveryResponse getSosSmsDelivery(
            @Parameter(description = "Id SOS-события, возвращенный POST /api/sos.", example = "3b06b36f-8077-4f03-b8cf-bb7a7b9b6f6f", required = true)
            @PathVariable UUID sosId
    ) {
        return smsService.getSosSmsDelivery(sosId);
    }
}
