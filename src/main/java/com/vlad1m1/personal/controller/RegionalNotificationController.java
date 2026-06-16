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

    @Operation(summary = "Получить региональные уведомления", description = "Возвращает активные уведомления региона. Параметр severity фильтрует списки WARNING или CRITICAL. Результаты отсортированы по publishedAt по убыванию.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Уведомления возвращены",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = RegionalNotificationResponse.class),
                            examples = {
                                    @ExampleObject(name = "severity=WARNING", value = OpenApiExamples.NOTIFICATIONS_WARNING_RESPONSE),
                                    @ExampleObject(name = "severity=CRITICAL", value = OpenApiExamples.NOTIFICATIONS_CRITICAL_RESPONSE)
                            })),
            @ApiResponse(responseCode = "400", description = "Некорректный query-параметр",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class), examples = @ExampleObject(value = OpenApiExamples.VALIDATION_ERROR))),
            @ApiResponse(responseCode = "404", description = "Регион не найден",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class), examples = @ExampleObject(value = OpenApiExamples.NOT_FOUND_ERROR))),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class), examples = @ExampleObject(value = OpenApiExamples.INTERNAL_ERROR)))
    })
    @GetMapping
    public List<RegionalNotificationResponse> getNotifications(
            @Parameter(description = "Обязательный id региона.", example = "1", required = true)
            @RequestParam Long regionId,
            @Parameter(description = "Необязательный фильтр важности.", example = "WARNING")
            @RequestParam(required = false) NotificationSeverity severity,
            @Parameter(description = "Максимальное количество элементов. По умолчанию 50, максимум 100.", example = "20")
            @RequestParam(required = false) Integer limit
    ) {
        return regionalNotificationService.getByRegion(regionId, severity, limit);
    }

    @Operation(summary = "Получить последние уведомления региона", description = "Возвращает последние активные уведомления для главного экрана приложения и компактных warning-виджетов.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Последние уведомления возвращены",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = RegionalNotificationResponse.class),
                            examples = @ExampleObject(name = "Latest notifications", value = OpenApiExamples.NOTIFICATIONS_WARNING_RESPONSE))),
            @ApiResponse(responseCode = "404", description = "Регион не найден",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class), examples = @ExampleObject(value = OpenApiExamples.NOT_FOUND_ERROR))),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class), examples = @ExampleObject(value = OpenApiExamples.INTERNAL_ERROR)))
    })
    @GetMapping("/latest")
    public List<RegionalNotificationResponse> getLatestNotifications(
            @Parameter(description = "Обязательный id региона.", example = "1", required = true)
            @RequestParam Long regionId
    ) {
        return regionalNotificationService.getLatestByRegion(regionId);
    }

    @Operation(summary = "Получить региональное уведомление", description = "Диагностический endpoint для открытия или логирования конкретного уведомления по id.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Уведомление возвращено",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = RegionalNotificationResponse.class),
                            examples = @ExampleObject(name = "Notification", value = OpenApiExamples.NOTIFICATION_RESPONSE))),
            @ApiResponse(responseCode = "404", description = "Уведомление не найдено",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class), examples = @ExampleObject(value = OpenApiExamples.NOT_FOUND_ERROR))),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class), examples = @ExampleObject(value = OpenApiExamples.INTERNAL_ERROR)))
    })
    @GetMapping("/{id}")
    public RegionalNotificationResponse getNotificationById(
            @Parameter(description = "Идентификатор уведомления.", example = "d8df5b89-6845-4b2f-ae58-b3c6c8edc7fb", required = true)
            @PathVariable UUID id
    ) {
        return regionalNotificationService.getById(id);
    }
}
