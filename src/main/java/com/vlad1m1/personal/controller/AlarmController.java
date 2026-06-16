package com.vlad1m1.personal.controller;

import com.vlad1m1.personal.config.OpenApiExamples;
import com.vlad1m1.personal.dto.AlarmResponse;
import com.vlad1m1.personal.dto.ApiErrorResponse;
import com.vlad1m1.personal.service.AlarmService;
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
@RequestMapping("/api/alarms")
@Tag(name = "Public Alarms")
public class AlarmController {
    private final AlarmService alarmService;

    public AlarmController(AlarmService alarmService) {
        this.alarmService = alarmService;
    }

    @Operation(summary = "Получить тревожные уведомления", description = "Возвращает тревожные уведомления. Для фильтрации по региону можно передать regionId или region_id.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Уведомления возвращены",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = AlarmResponse.class),
                            examples = @ExampleObject(name = "Alarms", value = OpenApiExamples.ALARMS_RESPONSE))),
            @ApiResponse(responseCode = "404", description = "Регион не найден",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class), examples = @ExampleObject(value = OpenApiExamples.NOT_FOUND_ERROR))),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class), examples = @ExampleObject(value = OpenApiExamples.INTERNAL_ERROR)))
    })
    @GetMapping
    public List<AlarmResponse> getAlarms(
            @Parameter(description = "Идентификатор региона.", example = "1")
            @RequestParam(required = false) Long regionId,
            @Parameter(description = "Идентификатор региона в snake_case.", example = "1")
            @RequestParam(name = "region_id", required = false) Long regionIdSnake
    ) {
        return alarmService.getAllAlarms(resolve(regionId, regionIdSnake));
    }

    @Operation(summary = "Получить тревожное уведомление", description = "Возвращает одно тревожное уведомление по UUID.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Уведомление возвращено",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = AlarmResponse.class),
                            examples = @ExampleObject(name = "Alarm", value = OpenApiExamples.ALARM_RESPONSE))),
            @ApiResponse(responseCode = "404", description = "Уведомление не найдено",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class), examples = @ExampleObject(value = OpenApiExamples.NOT_FOUND_ERROR))),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class), examples = @ExampleObject(value = OpenApiExamples.INTERNAL_ERROR)))
    })
    @GetMapping("/{id}")
    public AlarmResponse getAlarmById(
            @Parameter(description = "Идентификатор тревожного уведомления.", example = "5f2f8715-2c4f-4ab8-a450-43e693d6641d", required = true)
            @PathVariable UUID id
    ) {
        return alarmService.getAlarmById(id);
    }

    private Long resolve(Long camelCase, Long snakeCase) {
        return camelCase != null ? camelCase : snakeCase;
    }
}
