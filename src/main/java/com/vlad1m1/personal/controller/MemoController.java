package com.vlad1m1.personal.controller;

import com.vlad1m1.personal.config.OpenApiExamples;
import com.vlad1m1.personal.dto.ApiErrorResponse;
import com.vlad1m1.personal.dto.MemoDetailResponse;
import com.vlad1m1.personal.dto.MemoSummaryResponse;
import com.vlad1m1.personal.dto.MemoUpdatesResponse;
import com.vlad1m1.personal.service.MemoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/memos")
@Tag(name = "Public Memos")
public class MemoController {
    private final MemoService memoService;

    public MemoController(MemoService memoService) {
        this.memoService = memoService;
    }

    @Operation(summary = "Получить список памяток", description = "Возвращает памятки для каталога. По умолчанию выдаются активные глобальные памятки; при переданном regionId добавляются памятки этого региона. Доступны фильтры categoryId/category_id, isActive/is_active и isCritical/is_critical.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Список памяток возвращен",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = MemoSummaryResponse.class),
                            examples = @ExampleObject(name = "Memo list", value = OpenApiExamples.MEMOS_RESPONSE))),
            @ApiResponse(responseCode = "404", description = "Регион или категория не найдены",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class), examples = @ExampleObject(value = OpenApiExamples.NOT_FOUND_ERROR))),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class), examples = @ExampleObject(value = OpenApiExamples.INTERNAL_ERROR)))
    })
    @GetMapping
    public List<MemoSummaryResponse> getMemos(
            @Parameter(description = "Необязательный id региона. Если указан, ответ включает глобальные памятки и памятки этого региона.", example = "1")
            @RequestParam(required = false) Long regionId,
            @Parameter(description = "Необязательный id региона в snake_case.", example = "1")
            @RequestParam(name = "region_id", required = false) Long regionIdSnake,
            @Parameter(description = "Необязательный фильтр по id категории.", example = "1")
            @RequestParam(required = false) Long categoryId,
            @Parameter(description = "Необязательный id категории в snake_case.", example = "1")
            @RequestParam(name = "category_id", required = false) Long categoryIdSnake,
            @Parameter(description = "Необязательный фильтр активности. По умолчанию для публичного каталога используется true.", example = "true")
            @RequestParam(required = false) Boolean isActive,
            @Parameter(description = "Необязательный фильтр активности в snake_case.", example = "true")
            @RequestParam(name = "is_active", required = false) Boolean isActiveSnake,
            @Parameter(description = "Необязательный фильтр критичности.", example = "true")
            @RequestParam(required = false) Boolean isCritical,
            @Parameter(description = "Необязательный фильтр критичности в snake_case.", example = "true")
            @RequestParam(name = "is_critical", required = false) Boolean isCriticalSnake
    ) {
        return memoService.getMemoSummaries(
                resolve(regionId, regionIdSnake),
                resolve(categoryId, categoryIdSnake),
                resolve(isActive, isActiveSnake),
                resolve(isCritical, isCriticalSnake)
        );
    }

    @Operation(summary = "Получить обновления памяток", description = "Возвращает памятки, измененные после since, для offline-синхронизации. Для обновления локального кэша используйте contentHash, version и updatedAt.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Обновления памяток возвращены",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = MemoUpdatesResponse.class),
                            examples = @ExampleObject(name = "Memo updates", value = OpenApiExamples.MEMO_UPDATES_RESPONSE))),
            @ApiResponse(responseCode = "400", description = "Некорректный since или regionId",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class), examples = @ExampleObject(value = OpenApiExamples.VALIDATION_ERROR))),
            @ApiResponse(responseCode = "404", description = "Регион не найден",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class), examples = @ExampleObject(value = OpenApiExamples.NOT_FOUND_ERROR))),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class), examples = @ExampleObject(value = OpenApiExamples.INTERNAL_ERROR)))
    })
    @GetMapping("/updates")
    public MemoUpdatesResponse getMemoUpdates(
            @Parameter(description = "Вернуть только памятки, обновленные после этой метки времени. Не передавайте для полной синхронизации.", example = "2026-05-12T10:00:00")
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime since,
            @Parameter(description = "Необязательный id региона для синхронизации региональных памяток.", example = "1")
            @RequestParam(required = false) Long regionId
    ) {
        return memoService.getMemoUpdates(since, regionId);
    }

    @Operation(summary = "Получить детали памятки", description = "Возвращает полное содержимое памятки, включая htmlContent для Flutter WebView. Для проверки кэша используйте contentHash, version и updatedAt.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Детали памятки возвращены",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = MemoDetailResponse.class),
                            examples = @ExampleObject(name = "Memo details", value = OpenApiExamples.MEMO_DETAIL_RESPONSE))),
            @ApiResponse(responseCode = "404", description = "Памятка не найдена",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class), examples = @ExampleObject(value = OpenApiExamples.NOT_FOUND_ERROR))),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class), examples = @ExampleObject(value = OpenApiExamples.INTERNAL_ERROR)))
    })
    @GetMapping("/{id}")
    public MemoDetailResponse getMemoById(
            @Parameter(description = "Идентификатор памятки из списка или ленты обновлений.", example = "8c7a15f8-2f68-4d8d-8cbb-2b26f1c4b4d3", required = true)
            @PathVariable UUID id
    ) {
        return memoService.getMemoDetail(id);
    }

    private <T> T resolve(T camelCase, T snakeCase) {
        return camelCase != null ? camelCase : snakeCase;
    }
}
