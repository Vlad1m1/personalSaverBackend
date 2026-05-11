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

    @Operation(
            summary = "Проверить доступность backend",
            description = "Простой smoke-check endpoint для мобильных разработчиков, CI и диагностики деплоя."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Сервис доступен",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = HealthResponse.class),
                            examples = @ExampleObject(name = "Состояние сервиса", value = OpenApiExamples.HEALTH_RESPONSE))),
            @ApiResponse(responseCode = "500", description = "Непредвиденная ошибка сервера",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ApiErrorResponse.class),
                            examples = @ExampleObject(name = "Ошибка сервера", value = OpenApiExamples.INTERNAL_ERROR)))
    })
    @GetMapping
    public HealthResponse getHealth() {
        return new HealthResponse("UP", "personal-rescue-backend", OffsetDateTime.now());
    }
}
