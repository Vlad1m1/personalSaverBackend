package com.vlad1m1.personal.controller;

import com.vlad1m1.personal.config.OpenApiExamples;
import com.vlad1m1.personal.dto.ApiErrorResponse;
import com.vlad1m1.personal.dto.MemoDetailResponse;
import com.vlad1m1.personal.dto.MemoSummaryResponse;
import com.vlad1m1.personal.dto.MemoUpdatesResponse;
import com.vlad1m1.personal.service.MemoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
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
@Tag(name = "Memos")
public class MemoController {

    private final MemoService memoService;

    public MemoController(MemoService memoService) {
        this.memoService = memoService;
    }

    @Operation(
            summary = "Получить памятки для каталога",
            description = """
                    Возвращает краткие данные активных памяток. Без regionId endpoint возвращает только общие памятки.
                    С regionId возвращаются общие памятки плюс региональные памятки выбранного региона.
                    htmlContent в списке не возвращается; для HTML-контента WebView используйте GET /api/memos/{id}.
                    """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Список памяток успешно возвращен",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            array = @ArraySchema(schema = @Schema(implementation = MemoSummaryResponse.class)),
                            examples = @ExampleObject(name = "Памятки", value = OpenApiExamples.MEMOS_RESPONSE))),
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
    public List<MemoSummaryResponse> getMemos(
            @Parameter(description = "Необязательный id региона. Если указан, к общим памяткам добавляются региональные.", example = "1")
            @RequestParam(required = false) Long regionId
    ) {
        return memoService.getMemoSummaries(regionId);
    }

    @Operation(
            summary = "Получить обновления памяток для офлайн-синхронизации",
            description = """
                    Используется мобильным приложением для обновления офлайн-кэша памяток. since - ISO-8601 timestamp
                    последней успешной синхронизации. regionId работает так же, как в GET /api/memos: без него
                    возвращаются только общие памятки, с ним - общие и региональные обновления.
                    """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Данные для синхронизации успешно возвращены",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = MemoUpdatesResponse.class),
                            examples = @ExampleObject(name = "Обновления памяток", value = OpenApiExamples.MEMO_UPDATES_RESPONSE))),
            @ApiResponse(responseCode = "400", description = "Некорректный timestamp в параметре since",
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
    @GetMapping("/updates")
    public MemoUpdatesResponse getMemoUpdates(
            @Parameter(description = "Необязательный ISO-8601 timestamp. Возвращаются только памятки, обновленные после этого момента.", example = "2026-05-01T00:00:00")
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime since,

            @Parameter(description = "Необязательный id региона для включения региональных обновлений памяток.", example = "1")
            @RequestParam(required = false)
            Long regionId
    ) {
        return memoService.getMemoUpdates(since, regionId);
    }

    @Operation(
            summary = "Получить памятку с HTML для WebView",
            description = """
                    Возвращает полную памятку. htmlContent - готовая HTML-страница или HTML-фрагмент,
                    который можно напрямую отобразить во Flutter WebView. Используйте этот endpoint после
                    открытия памятки пользователем из списка.
                    """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Полная памятка успешно возвращена",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = MemoDetailResponse.class),
                            examples = @ExampleObject(name = "Полная памятка", value = OpenApiExamples.MEMO_DETAIL_RESPONSE))),
            @ApiResponse(responseCode = "404", description = "Памятка не найдена",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ApiErrorResponse.class),
                            examples = @ExampleObject(name = "Не найдено", value = OpenApiExamples.NOT_FOUND_ERROR))),
            @ApiResponse(responseCode = "500", description = "Непредвиденная ошибка сервера",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ApiErrorResponse.class),
                            examples = @ExampleObject(name = "Ошибка сервера", value = OpenApiExamples.INTERNAL_ERROR)))
    })
    @GetMapping("/{id}")
    public MemoDetailResponse getMemoById(
            @Parameter(description = "Id памятки из GET /api/memos или /api/memos/updates.", example = "8c7a15f8-2f68-4d8d-8cbb-2b26f1c4b4d3", required = true)
            @PathVariable UUID id
    ) {
        return memoService.getMemoDetail(id);
    }
}
