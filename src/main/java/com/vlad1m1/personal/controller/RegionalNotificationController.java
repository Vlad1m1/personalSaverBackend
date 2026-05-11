package com.vlad1m1.personal.controller;

import com.vlad1m1.personal.config.OpenApiExamples;
import com.vlad1m1.personal.dto.ApiErrorResponse;
import com.vlad1m1.personal.dto.RegionalNotificationResponse;
import com.vlad1m1.personal.service.RegionalNotificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
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
@Tag(name = "Regional Notifications")
public class RegionalNotificationController {

    private final RegionalNotificationService regionalNotificationService;

    public RegionalNotificationController(RegionalNotificationService regionalNotificationService) {
        this.regionalNotificationService = regionalNotificationService;
    }

    @Operation(
            summary = "Получить региональные уведомления",
            description = """
                    Возвращает активные уведомления выбранного региона. Уведомления привязаны к региону,
                    поэтому Flutter должен передавать текущий regionId, выбранный при онбординге или в настройках профиля.
                    """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Региональные уведомления успешно возвращены",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            array = @ArraySchema(schema = @Schema(implementation = RegionalNotificationResponse.class)),
                            examples = @ExampleObject(name = "Региональные уведомления", value = OpenApiExamples.NOTIFICATIONS_RESPONSE))),
            @ApiResponse(responseCode = "400", description = "Параметр regionId отсутствует или некорректен",
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
    @GetMapping
    public List<RegionalNotificationResponse> getNotifications(
            @Parameter(description = "Id региона, для которого нужно вернуть уведомления.", example = "1", required = true)
            @RequestParam Long regionId
    ) {
        return regionalNotificationService.getByRegion(regionId);
    }

    @Operation(
            summary = "Получить самые свежие региональные уведомления",
            description = """
                    Возвращает самые новые активные уведомления региона. Endpoint предназначен для старта приложения,
                    бейджей на главном экране и легких обновлений, когда нужны только свежие предупреждения.
                    """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Свежие уведомления успешно возвращены",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            array = @ArraySchema(schema = @Schema(implementation = RegionalNotificationResponse.class)),
                            examples = @ExampleObject(name = "Свежие уведомления", value = OpenApiExamples.NOTIFICATIONS_RESPONSE))),
            @ApiResponse(responseCode = "400", description = "Параметр regionId отсутствует или некорректен",
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
    @GetMapping("/latest")
    public List<RegionalNotificationResponse> getLatestNotifications(
            @Parameter(description = "Id региона, для которого нужно вернуть самые свежие уведомления.", example = "1", required = true)
            @RequestParam Long regionId
    ) {
        return regionalNotificationService.getLatestByRegion(regionId);
    }

    @Operation(
            summary = "Получить уведомление по id",
            description = "Возвращает одно региональное уведомление для экрана деталей или диагностики."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Уведомление успешно возвращено",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = RegionalNotificationResponse.class),
                            examples = @ExampleObject(name = "Уведомление", value = OpenApiExamples.NOTIFICATION_RESPONSE))),
            @ApiResponse(responseCode = "404", description = "Уведомление не найдено",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ApiErrorResponse.class),
                            examples = @ExampleObject(name = "Не найдено", value = OpenApiExamples.NOT_FOUND_ERROR))),
            @ApiResponse(responseCode = "500", description = "Непредвиденная ошибка сервера",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ApiErrorResponse.class),
                            examples = @ExampleObject(name = "Ошибка сервера", value = OpenApiExamples.INTERNAL_ERROR)))
    })
    @GetMapping("/{id}")
    public RegionalNotificationResponse getNotificationById(
            @Parameter(description = "Id уведомления из GET /api/notifications.", example = "d8df5b89-6845-4b2f-ae58-b3c6c8edc7fb", required = true)
            @PathVariable UUID id
    ) {
        return regionalNotificationService.getById(id);
    }
}
