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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
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
            summary = "Создать SOS-событие",
            description = """
                    Создает SOS-событие из Flutter-приложения и запускает доставку SMS.

                    Ограничение по умолчанию: 5 POST /api/sos запросов в минуту с одного IP-адреса. При превышении возвращается 429 Too Many Requests.

                    Если координаты переданы, они строго валидируются: latitude от -90 до 90, longitude от -180 до 180.
                    message обязателен и ограничен 500 символами. contactPhone нормализуется перед маршрутизацией.

                    Backend не знает текущего пользователя приложения и не хранит список контактов. Для EMERGENCY_CONTACT
                    телефон выбранного контакта передается только в этом запросе. Для EMERGENCY_SERVICE backend использует телефон региона
                    или резервный номер 112.
                    """
    )
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Полезная нагрузка SOS, сформированная мобильным приложением.",
            required = true,
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = SosRequest.class),
                    examples = {
                            @ExampleObject(name = "SOS to emergency contact", value = OpenApiExamples.SOS_CONTACT_REQUEST),
                            @ExampleObject(name = "SOS to emergency service", value = OpenApiExamples.SOS_SERVICE_REQUEST)
                    })
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "SOS-событие создано, SMS принято к доставке",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = SosResponse.class),
                            examples = @ExampleObject(name = "Created SOS event", value = OpenApiExamples.SOS_RESPONSE))),
            @ApiResponse(responseCode = "400", description = "Ошибка валидации",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ApiErrorResponse.class),
                            examples = @ExampleObject(name = "Validation Error", value = OpenApiExamples.VALIDATION_ERROR))),
            @ApiResponse(responseCode = "404", description = "Регион не найден",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ApiErrorResponse.class),
                            examples = @ExampleObject(name = "Not Found", value = OpenApiExamples.NOT_FOUND_ERROR))),
            @ApiResponse(responseCode = "429", description = "Слишком много запросов",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ApiErrorResponse.class),
                            examples = @ExampleObject(name = "Rate limit", value = OpenApiExamples.RATE_LIMIT_ERROR))),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера",
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

    @Operation(summary = "Получить диагностику SOS", description = "Диагностический endpoint конкретного SOS-события. Используйте его, когда приложению нужно показать или залогировать итоговый статус SOS/SMS.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "SOS-событие возвращено",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = SosResponse.class),
                            examples = @ExampleObject(name = "SOS result", value = OpenApiExamples.SOS_RESPONSE))),
            @ApiResponse(responseCode = "404", description = "SOS-событие не найдено",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ApiErrorResponse.class),
                            examples = @ExampleObject(name = "Not Found", value = OpenApiExamples.NOT_FOUND_ERROR))),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ApiErrorResponse.class),
                            examples = @ExampleObject(name = "Internal Server Error", value = OpenApiExamples.INTERNAL_ERROR)))
    })
    @GetMapping("/{id}")
    public SosResponse getSosById(
            @Parameter(description = "Идентификатор SOS-события, возвращенный POST /api/sos.", example = "3b06b36f-8077-4f03-b8cf-bb7a7b9b6f6f", required = true)
            @PathVariable UUID id
    ) {
        return sosService.getById(id);
    }

    @Operation(summary = "Получить список SOS-событий", description = "Возвращает созданные SOS-события. Необязательный regionId/region_id фильтрует события по региону.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "SOS-события возвращены",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = SosResponse.class),
                            examples = @ExampleObject(name = "SOS events", value = OpenApiExamples.SOS_EVENTS_RESPONSE))),
            @ApiResponse(responseCode = "404", description = "Регион не найден",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ApiErrorResponse.class),
                            examples = @ExampleObject(name = "Not Found", value = OpenApiExamples.NOT_FOUND_ERROR))),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ApiErrorResponse.class),
                            examples = @ExampleObject(name = "Internal Server Error", value = OpenApiExamples.INTERNAL_ERROR)))
    })
    @GetMapping
    public List<SosResponse> getSosEvents(
            @Parameter(description = "Необязательный id региона.", example = "1")
            @RequestParam(required = false) Long regionId,
            @Parameter(description = "Необязательный id региона в snake_case.", example = "1")
            @RequestParam(name = "region_id", required = false) Long regionIdSnake
    ) {
        return sosService.getEvents(regionId != null ? regionId : regionIdSnake);
    }
}
