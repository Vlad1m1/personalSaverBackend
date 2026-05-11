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
@Tag(name = "SOS")
public class SosController {

    private final SosService sosService;

    public SosController(SosService sosService) {
        this.sosService = sosService;
    }

    @Operation(
            summary = "Создать SOS-событие и отправить SMS",
            description = """
                    Создает SOS-событие по данным из Flutter-приложения и запускает SMS-доставку.
                    Backend не знает текущего пользователя и не хранит список его контактов.

                    Режимы доставки:
                    - EMERGENCY_SERVICE: backend отправляет SMS на region.emergencyPhone или на дефолтный 112, если у региона нет emergencyPhone. contactPhone игнорируется.
                    - EMERGENCY_CONTACT: backend отправляет SMS на contactPhone из запроса. contactPhone обязателен.

                    Сохраните возвращенный id, если приложению нужно позже проверить результат доставки через GET /api/sos/{id}.
                    """
    )
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "SOS-данные, сформированные мобильным приложением.",
            required = true,
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = SosRequest.class),
                    examples = {
                            @ExampleObject(name = "Запрос EMERGENCY_CONTACT", value = OpenApiExamples.SOS_CONTACT_REQUEST),
                            @ExampleObject(name = "Запрос EMERGENCY_SERVICE", value = OpenApiExamples.SOS_SERVICE_REQUEST)
                    })
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "SOS-событие создано, SMS принята к отправке",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = SosResponse.class),
                            examples = @ExampleObject(name = "Созданное SOS-событие", value = OpenApiExamples.SOS_RESPONSE))),
            @ApiResponse(responseCode = "400", description = "Ошибка валидации, например отсутствует contactPhone для EMERGENCY_CONTACT",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ApiErrorResponse.class),
                            examples = @ExampleObject(name = "Ошибка валидации", value = OpenApiExamples.VALIDATION_ERROR))),
            @ApiResponse(responseCode = "404", description = "Регион с таким regionId не найден",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ApiErrorResponse.class),
                            examples = @ExampleObject(name = "Не найдено", value = OpenApiExamples.NOT_FOUND_ERROR))),
            @ApiResponse(responseCode = "500", description = "Непредвиденная ошибка сервера",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ApiErrorResponse.class),
                            examples = @ExampleObject(name = "Ошибка сервера", value = OpenApiExamples.INTERNAL_ERROR)))
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SosResponse createSos(
            @org.springframework.web.bind.annotation.RequestBody @Valid SosRequest request
    ) {
        return sosService.create(request);
    }

    @Operation(
            summary = "Получить результат доставки SOS",
            description = """
                    Диагностический endpoint для конкретного SOS-события. Используйте его, когда Flutter-приложению
                    нужно показать или залогировать итоговое состояние доставки SOS/SMS.
                    """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "SOS-событие успешно возвращено",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = SosResponse.class),
                            examples = @ExampleObject(name = "Результат SOS", value = OpenApiExamples.SOS_RESPONSE))),
            @ApiResponse(responseCode = "404", description = "SOS-событие не найдено",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ApiErrorResponse.class),
                            examples = @ExampleObject(name = "Не найдено", value = OpenApiExamples.NOT_FOUND_ERROR))),
            @ApiResponse(responseCode = "500", description = "Непредвиденная ошибка сервера",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ApiErrorResponse.class),
                            examples = @ExampleObject(name = "Ошибка сервера", value = OpenApiExamples.INTERNAL_ERROR)))
    })
    @GetMapping("/{id}")
    public SosResponse getSosById(
            @Parameter(description = "Id SOS-события, возвращенный POST /api/sos.", example = "3b06b36f-8077-4f03-b8cf-bb7a7b9b6f6f", required = true)
            @PathVariable UUID id
    ) {
        return sosService.getById(id);
    }
}
