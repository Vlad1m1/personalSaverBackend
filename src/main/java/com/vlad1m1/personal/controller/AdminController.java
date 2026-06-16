package com.vlad1m1.personal.controller;

import com.vlad1m1.personal.config.OpenApiExamples;
import com.vlad1m1.personal.dto.AdminMemoCategoryCreateRequest;
import com.vlad1m1.personal.dto.AdminMemoCategoryUpdateRequest;
import com.vlad1m1.personal.dto.AdminMemoCreateRequest;
import com.vlad1m1.personal.dto.AdminMemoUpdateRequest;
import com.vlad1m1.personal.dto.AdminRegionCreateRequest;
import com.vlad1m1.personal.dto.AdminRegionUpdateRequest;
import com.vlad1m1.personal.dto.AlarmRequest;
import com.vlad1m1.personal.dto.AlarmResponse;
import com.vlad1m1.personal.dto.ApiErrorResponse;
import com.vlad1m1.personal.dto.MemoCategoryRequest;
import com.vlad1m1.personal.dto.MemoCategoryResponse;
import com.vlad1m1.personal.dto.MemoDetailResponse;
import com.vlad1m1.personal.dto.MemoRequest;
import com.vlad1m1.personal.dto.NotificationParsingLogResponse;
import com.vlad1m1.personal.dto.RegionRequest;
import com.vlad1m1.personal.dto.RegionResponse;
import com.vlad1m1.personal.service.AlarmService;
import com.vlad1m1.personal.service.CategoryService;
import com.vlad1m1.personal.service.MemoService;
import com.vlad1m1.personal.service.NotificationParsingService;
import com.vlad1m1.personal.service.RegionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/admin")
@SecurityRequirement(name = "AdminApiKey")
public class AdminController {
    private final RegionService regionService;
    private final CategoryService categoryService;
    private final MemoService memoService;
    private final AlarmService alarmService;
    private final NotificationParsingService notificationParsingService;

    public AdminController(RegionService regionService, CategoryService categoryService, MemoService memoService, AlarmService alarmService, NotificationParsingService notificationParsingService) {
        this.regionService = regionService;
        this.categoryService = categoryService;
        this.memoService = memoService;
        this.alarmService = alarmService;
        this.notificationParsingService = notificationParsingService;
    }

    @Tag(name = "Admin Regions")
    @Operation(summary = "Создать регион", description = "Создает регион. Endpoint защищен техническим X-Admin-Api-Key; это не пользовательская авторизация и не JWT.")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(required = true, content = @Content(schema = @Schema(implementation = AdminRegionCreateRequest.class), examples = @ExampleObject(value = OpenApiExamples.ADMIN_REGION_CREATE_REQUEST)))
    @AdminResponses(created = true)
    @PostMapping("/regions")
    @ResponseStatus(HttpStatus.CREATED)
    public RegionResponse createRegion(@org.springframework.web.bind.annotation.RequestBody @Valid AdminRegionCreateRequest request) {
        return regionService.createRegion(new RegionRequest(request.name(), request.emergencyPhone()));
    }

    @Tag(name = "Admin Regions")
    @Operation(summary = "Обновить регион", description = "Обновляет регион, который используется публичным каталогом, уведомлениями и SOS-маршрутизацией.")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(required = true, content = @Content(schema = @Schema(implementation = AdminRegionUpdateRequest.class), examples = @ExampleObject(value = OpenApiExamples.ADMIN_REGION_CREATE_REQUEST)))
    @AdminResponses
    @PatchMapping("/regions/{id}")
    public RegionResponse updateRegion(
            @Parameter(description = "Идентификатор региона.", example = "1", required = true) @PathVariable Long id,
            @org.springframework.web.bind.annotation.RequestBody @Valid AdminRegionUpdateRequest request
    ) {
        return regionService.updateRegion(id, new RegionRequest(request.name(), request.emergencyPhone()));
    }

    @Tag(name = "Admin Regions")
    @Operation(summary = "Удалить регион", description = "Удаляет регион. Возвращает 409, если удалению мешают связи в базе данных.")
    @AdminResponses(noContent = true)
    @DeleteMapping("/regions/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteRegion(@Parameter(description = "Идентификатор региона.", example = "1", required = true) @PathVariable Long id) {
        regionService.deleteRegion(id);
    }

    @Tag(name = "Admin Memo Categories")
    @Operation(summary = "Создать категорию памяток", description = "Создает категорию для группировки публичных памяток.")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(required = true, content = @Content(schema = @Schema(implementation = AdminMemoCategoryCreateRequest.class), examples = @ExampleObject(value = OpenApiExamples.ADMIN_CATEGORY_REQUEST)))
    @AdminResponses(created = true)
    @PostMapping("/memo-categories")
    @ResponseStatus(HttpStatus.CREATED)
    public MemoCategoryResponse createCategory(@org.springframework.web.bind.annotation.RequestBody @Valid AdminMemoCategoryCreateRequest request) {
        return categoryService.createCategory(new MemoCategoryRequest(request.name(), request.iconName(), request.accentColor(), request.displayOrder()));
    }

    @Tag(name = "Admin Memo Categories")
    @Operation(summary = "Обновить категорию памяток", description = "Обновляет метаданные категории, используемые мобильным каталогом памяток.")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(required = true, content = @Content(schema = @Schema(implementation = AdminMemoCategoryUpdateRequest.class), examples = @ExampleObject(value = OpenApiExamples.ADMIN_CATEGORY_REQUEST)))
    @AdminResponses
    @PatchMapping("/memo-categories/{id}")
    public MemoCategoryResponse updateCategory(
            @Parameter(description = "Идентификатор категории.", example = "1", required = true) @PathVariable Long id,
            @org.springframework.web.bind.annotation.RequestBody @Valid AdminMemoCategoryUpdateRequest request
    ) {
        return categoryService.updateCategory(id, new MemoCategoryRequest(request.name(), request.iconName(), request.accentColor(), request.displayOrder()));
    }

    @Tag(name = "Admin Memo Categories")
    @Operation(summary = "Удалить категорию памяток", description = "Удаляет категорию памяток. Возвращает 409, если на нее ссылаются существующие памятки.")
    @AdminResponses(noContent = true)
    @DeleteMapping("/memo-categories/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCategory(@Parameter(description = "Идентификатор категории.", example = "1", required = true) @PathVariable Long id) {
        categoryService.deleteCategory(id);
    }

    @Tag(name = "Admin Memos")
    @Operation(summary = "Создать памятку", description = "Создает черновик или опубликованную памятку. Публичные списки по умолчанию возвращают только активные памятки.")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(required = true, content = @Content(schema = @Schema(implementation = AdminMemoCreateRequest.class), examples = @ExampleObject(value = OpenApiExamples.ADMIN_MEMO_REQUEST)))
    @AdminResponses(created = true)
    @PostMapping("/memos")
    @ResponseStatus(HttpStatus.CREATED)
    public MemoDetailResponse createMemo(@org.springframework.web.bind.annotation.RequestBody @Valid AdminMemoCreateRequest request) {
        return memoService.createMemo(toMemoRequest(request));
    }

    @Tag(name = "Admin Memos")
    @Operation(summary = "Обновить памятку", description = "Обновляет содержимое памятки, метаданные, версию и флаг публикации.")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(required = true, content = @Content(schema = @Schema(implementation = AdminMemoUpdateRequest.class), examples = @ExampleObject(value = OpenApiExamples.ADMIN_MEMO_REQUEST)))
    @AdminResponses
    @PatchMapping("/memos/{id}")
    public MemoDetailResponse updateMemo(
            @Parameter(description = "Идентификатор памятки.", example = "8c7a15f8-2f68-4d8d-8cbb-2b26f1c4b4d3", required = true) @PathVariable UUID id,
            @org.springframework.web.bind.annotation.RequestBody @Valid AdminMemoUpdateRequest request
    ) {
        return memoService.updateMemo(id, toMemoRequest(request));
    }

    @Tag(name = "Admin Memos")
    @Operation(summary = "Удалить памятку", description = "Удаляет памятку по id.")
    @AdminResponses(noContent = true)
    @DeleteMapping("/memos/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteMemo(@Parameter(description = "Идентификатор памятки.", example = "8c7a15f8-2f68-4d8d-8cbb-2b26f1c4b4d3", required = true) @PathVariable UUID id) {
        memoService.deleteMemo(id);
    }

    @Tag(name = "Admin Memos")
    @Operation(summary = "Опубликовать памятку", description = "Помечает памятку как активную, чтобы она появилась в публичных endpoints памяток.")
    @AdminResponses
    @PostMapping("/memos/{id}/publish")
    public MemoDetailResponse publishMemo(@Parameter(description = "Идентификатор памятки.", example = "8c7a15f8-2f68-4d8d-8cbb-2b26f1c4b4d3", required = true) @PathVariable UUID id) {
        return memoService.publishMemo(id);
    }

    @Tag(name = "Admin Memos")
    @Operation(summary = "Снять памятку с публикации", description = "Помечает памятку как неактивную, чтобы она больше не отображалась в публичных endpoints по умолчанию.")
    @AdminResponses
    @PostMapping("/memos/{id}/unpublish")
    public MemoDetailResponse unpublishMemo(@Parameter(description = "Идентификатор памятки.", example = "8c7a15f8-2f68-4d8d-8cbb-2b26f1c4b4d3", required = true) @PathVariable UUID id) {
        return memoService.unpublishMemo(id);
    }

    @Tag(name = "Admin Alarms")
    @Operation(summary = "Создать тревожное уведомление", description = "Создает подробное тревожное уведомление для региона.")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(required = true, content = @Content(schema = @Schema(implementation = AlarmRequest.class), examples = @ExampleObject(value = OpenApiExamples.ADMIN_ALARM_REQUEST)))
    @AdminResponses(created = true)
    @PostMapping("/alarms")
    @ResponseStatus(HttpStatus.CREATED)
    public AlarmResponse createAlarm(@org.springframework.web.bind.annotation.RequestBody @Valid AlarmRequest request) {
        return alarmService.createAlarm(request);
    }

    @Tag(name = "Admin Alarms")
    @Operation(summary = "Обновить тревожное уведомление", description = "Обновляет тревожное уведомление региона.")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(required = true, content = @Content(schema = @Schema(implementation = AlarmRequest.class), examples = @ExampleObject(value = OpenApiExamples.ADMIN_ALARM_REQUEST)))
    @AdminResponses
    @PatchMapping("/alarms/{id}")
    public AlarmResponse updateAlarm(
            @Parameter(description = "Идентификатор тревожного уведомления.", example = "5f2f8715-2c4f-4ab8-a450-43e693d6641d", required = true) @PathVariable UUID id,
            @org.springframework.web.bind.annotation.RequestBody @Valid AlarmRequest request
    ) {
        return alarmService.updateAlarm(id, request);
    }

    @Tag(name = "Admin Alarms")
    @Operation(summary = "Удалить тревожное уведомление", description = "Удаляет тревожное уведомление региона.")
    @AdminResponses(noContent = true)
    @DeleteMapping("/alarms/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAlarm(@Parameter(description = "Идентификатор тревожного уведомления.", example = "5f2f8715-2c4f-4ab8-a450-43e693d6641d", required = true) @PathVariable UUID id) {
        alarmService.deleteAlarm(id);
    }

    @Tag(name = "Admin Notifications")
    @Operation(summary = "Запустить парсинг уведомлений", description = "Запускает ручной парсинг региональных уведомлений и возвращает запись журнала.")
    @AdminResponses
    @PostMapping("/notifications/parse-now")
    public NotificationParsingLogResponse parseNotificationsNow() {
        return notificationParsingService.parseNow();
    }

    @Tag(name = "Admin Notifications")
    @Operation(summary = "Получить журнал парсинга уведомлений", description = "Возвращает последние журналы ручного парсинга уведомлений для диагностики администратора.")
    @AdminResponses
    @GetMapping("/notifications/parsing-logs")
    public List<NotificationParsingLogResponse> getParsingLogs() {
        return notificationParsingService.getLogs();
    }

    private MemoRequest toMemoRequest(AdminMemoCreateRequest request) {
        return new MemoRequest(request.categoryId(), request.regionId(), request.title(), request.shortDescription(), request.htmlContent(), request.steps(), request.version(), request.critical(), request.imageUrl(), request.iconName(), request.accentColor(), request.active());
    }

    private MemoRequest toMemoRequest(AdminMemoUpdateRequest request) {
        return new MemoRequest(request.categoryId(), request.regionId(), request.title(), request.shortDescription(), request.htmlContent(), request.steps(), request.version(), request.critical(), request.imageUrl(), request.iconName(), request.accentColor(), request.active());
    }

    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Успешно"),
            @ApiResponse(responseCode = "201", description = "Создано"),
            @ApiResponse(responseCode = "204", description = "Нет содержимого"),
            @ApiResponse(responseCode = "400", description = "Ошибка валидации", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ApiErrorResponse.class), examples = @ExampleObject(value = OpenApiExamples.VALIDATION_ERROR))),
            @ApiResponse(responseCode = "401", description = "Административный API-ключ отсутствует или некорректен", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ApiErrorResponse.class), examples = @ExampleObject(value = OpenApiExamples.ADMIN_KEY_ERROR))),
            @ApiResponse(responseCode = "404", description = "Не найдено", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ApiErrorResponse.class), examples = @ExampleObject(value = OpenApiExamples.NOT_FOUND_ERROR))),
            @ApiResponse(responseCode = "409", description = "Конфликт состояния", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ApiErrorResponse.class), examples = @ExampleObject(value = OpenApiExamples.CONFLICT_ERROR))),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ApiErrorResponse.class), examples = @ExampleObject(value = OpenApiExamples.INTERNAL_ERROR)))
    })
    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    private @interface AdminResponses {
        boolean created() default false;
        boolean noContent() default false;
    }
}
