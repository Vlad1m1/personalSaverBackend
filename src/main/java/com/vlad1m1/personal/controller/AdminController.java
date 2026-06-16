package com.vlad1m1.personal.controller;

import com.vlad1m1.personal.config.OpenApiExamples;
import com.vlad1m1.personal.dto.AdminMemoCategoryCreateRequest;
import com.vlad1m1.personal.dto.AdminMemoCategoryUpdateRequest;
import com.vlad1m1.personal.dto.AdminMemoCreateRequest;
import com.vlad1m1.personal.dto.AdminMemoUpdateRequest;
import com.vlad1m1.personal.dto.AdminRegionCreateRequest;
import com.vlad1m1.personal.dto.AdminRegionUpdateRequest;
import com.vlad1m1.personal.dto.ApiErrorResponse;
import com.vlad1m1.personal.dto.MemoCategoryRequest;
import com.vlad1m1.personal.dto.MemoCategoryResponse;
import com.vlad1m1.personal.dto.MemoDetailResponse;
import com.vlad1m1.personal.dto.MemoRequest;
import com.vlad1m1.personal.dto.NotificationParsingLogResponse;
import com.vlad1m1.personal.dto.RegionRequest;
import com.vlad1m1.personal.dto.RegionResponse;
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
    private final NotificationParsingService notificationParsingService;

    public AdminController(RegionService regionService, CategoryService categoryService, MemoService memoService, NotificationParsingService notificationParsingService) {
        this.regionService = regionService;
        this.categoryService = categoryService;
        this.memoService = memoService;
        this.notificationParsingService = notificationParsingService;
    }

    @Tag(name = "Admin Regions")
    @Operation(summary = "Create admin region", description = "Creates a region. Protected by technical X-Admin-Api-Key; this is not user authorization and not a JWT.")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(required = true, content = @Content(schema = @Schema(implementation = AdminRegionCreateRequest.class), examples = @ExampleObject(value = OpenApiExamples.ADMIN_REGION_CREATE_REQUEST)))
    @AdminResponses(created = true)
    @PostMapping("/regions")
    @ResponseStatus(HttpStatus.CREATED)
    public RegionResponse createRegion(@org.springframework.web.bind.annotation.RequestBody @Valid AdminRegionCreateRequest request) {
        return regionService.createRegion(new RegionRequest(request.name(), request.emergencyPhone()));
    }

    @Tag(name = "Admin Regions")
    @Operation(summary = "Update admin region", description = "Updates a region used by public catalog, notifications, and SOS routing.")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(required = true, content = @Content(schema = @Schema(implementation = AdminRegionUpdateRequest.class), examples = @ExampleObject(value = OpenApiExamples.ADMIN_REGION_CREATE_REQUEST)))
    @AdminResponses
    @PatchMapping("/regions/{id}")
    public RegionResponse updateRegion(
            @Parameter(description = "Region id.", example = "1", required = true) @PathVariable Long id,
            @org.springframework.web.bind.annotation.RequestBody @Valid AdminRegionUpdateRequest request
    ) {
        return regionService.updateRegion(id, new RegionRequest(request.name(), request.emergencyPhone()));
    }

    @Tag(name = "Admin Regions")
    @Operation(summary = "Delete admin region", description = "Deletes a region. Returns 409 if database constraints prevent deletion.")
    @AdminResponses(noContent = true)
    @DeleteMapping("/regions/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteRegion(@Parameter(description = "Region id.", example = "1", required = true) @PathVariable Long id) {
        regionService.deleteRegion(id);
    }

    @Tag(name = "Admin Memo Categories")
    @Operation(summary = "Create memo category", description = "Creates a category for grouping public memo content.")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(required = true, content = @Content(schema = @Schema(implementation = AdminMemoCategoryCreateRequest.class), examples = @ExampleObject(value = OpenApiExamples.ADMIN_CATEGORY_REQUEST)))
    @AdminResponses(created = true)
    @PostMapping("/memo-categories")
    @ResponseStatus(HttpStatus.CREATED)
    public MemoCategoryResponse createCategory(@org.springframework.web.bind.annotation.RequestBody @Valid AdminMemoCategoryCreateRequest request) {
        return categoryService.createCategory(new MemoCategoryRequest(request.name(), request.iconName(), request.accentColor(), request.displayOrder()));
    }

    @Tag(name = "Admin Memo Categories")
    @Operation(summary = "Update memo category", description = "Updates category metadata used by the mobile memo catalog.")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(required = true, content = @Content(schema = @Schema(implementation = AdminMemoCategoryUpdateRequest.class), examples = @ExampleObject(value = OpenApiExamples.ADMIN_CATEGORY_REQUEST)))
    @AdminResponses
    @PatchMapping("/memo-categories/{id}")
    public MemoCategoryResponse updateCategory(
            @Parameter(description = "Category id.", example = "1", required = true) @PathVariable Long id,
            @org.springframework.web.bind.annotation.RequestBody @Valid AdminMemoCategoryUpdateRequest request
    ) {
        return categoryService.updateCategory(id, new MemoCategoryRequest(request.name(), request.iconName(), request.accentColor(), request.displayOrder()));
    }

    @Tag(name = "Admin Memo Categories")
    @Operation(summary = "Delete memo category", description = "Deletes a memo category. Returns 409 if existing memos still reference it.")
    @AdminResponses(noContent = true)
    @DeleteMapping("/memo-categories/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCategory(@Parameter(description = "Category id.", example = "1", required = true) @PathVariable Long id) {
        categoryService.deleteCategory(id);
    }

    @Tag(name = "Admin Memos")
    @Operation(summary = "Create memo", description = "Creates a memo draft or published memo. Public list endpoints return only active memos.")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(required = true, content = @Content(schema = @Schema(implementation = AdminMemoCreateRequest.class), examples = @ExampleObject(value = OpenApiExamples.ADMIN_MEMO_REQUEST)))
    @AdminResponses(created = true)
    @PostMapping("/memos")
    @ResponseStatus(HttpStatus.CREATED)
    public MemoDetailResponse createMemo(@org.springframework.web.bind.annotation.RequestBody @Valid AdminMemoCreateRequest request) {
        return memoService.createMemo(toMemoRequest(request));
    }

    @Tag(name = "Admin Memos")
    @Operation(summary = "Update memo", description = "Updates memo content, metadata, version, and publication flag.")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(required = true, content = @Content(schema = @Schema(implementation = AdminMemoUpdateRequest.class), examples = @ExampleObject(value = OpenApiExamples.ADMIN_MEMO_REQUEST)))
    @AdminResponses
    @PatchMapping("/memos/{id}")
    public MemoDetailResponse updateMemo(
            @Parameter(description = "Memo id.", example = "8c7a15f8-2f68-4d8d-8cbb-2b26f1c4b4d3", required = true) @PathVariable UUID id,
            @org.springframework.web.bind.annotation.RequestBody @Valid AdminMemoUpdateRequest request
    ) {
        return memoService.updateMemo(id, toMemoRequest(request));
    }

    @Tag(name = "Admin Memos")
    @Operation(summary = "Delete memo", description = "Deletes a memo by id.")
    @AdminResponses(noContent = true)
    @DeleteMapping("/memos/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteMemo(@Parameter(description = "Memo id.", example = "8c7a15f8-2f68-4d8d-8cbb-2b26f1c4b4d3", required = true) @PathVariable UUID id) {
        memoService.deleteMemo(id);
    }

    @Tag(name = "Admin Memos")
    @Operation(summary = "Publish memo", description = "Marks a memo as active so it appears in public memo endpoints.")
    @AdminResponses
    @PostMapping("/memos/{id}/publish")
    public MemoDetailResponse publishMemo(@Parameter(description = "Memo id.", example = "8c7a15f8-2f68-4d8d-8cbb-2b26f1c4b4d3", required = true) @PathVariable UUID id) {
        return memoService.publishMemo(id);
    }

    @Tag(name = "Admin Memos")
    @Operation(summary = "Unpublish memo", description = "Marks a memo as inactive so it no longer appears in public memo endpoints.")
    @AdminResponses
    @PostMapping("/memos/{id}/unpublish")
    public MemoDetailResponse unpublishMemo(@Parameter(description = "Memo id.", example = "8c7a15f8-2f68-4d8d-8cbb-2b26f1c4b4d3", required = true) @PathVariable UUID id) {
        return memoService.unpublishMemo(id);
    }

    @Tag(name = "Admin Notifications")
    @Operation(summary = "Run notification parsing now", description = "Starts a manual regional notification parsing run and returns its log entry.")
    @AdminResponses
    @PostMapping("/notifications/parse-now")
    public NotificationParsingLogResponse parseNotificationsNow() {
        return notificationParsingService.parseNow();
    }

    @Tag(name = "Admin Notifications")
    @Operation(summary = "List notification parsing logs", description = "Returns recent manual notification parsing run logs for admin diagnostics.")
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
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "201", description = "Created"),
            @ApiResponse(responseCode = "204", description = "No Content"),
            @ApiResponse(responseCode = "400", description = "Validation Error", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ApiErrorResponse.class), examples = @ExampleObject(value = OpenApiExamples.VALIDATION_ERROR))),
            @ApiResponse(responseCode = "401", description = "Invalid or missing admin API key", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ApiErrorResponse.class), examples = @ExampleObject(value = OpenApiExamples.ADMIN_KEY_ERROR))),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ApiErrorResponse.class), examples = @ExampleObject(value = OpenApiExamples.NOT_FOUND_ERROR))),
            @ApiResponse(responseCode = "409", description = "Conflict", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ApiErrorResponse.class), examples = @ExampleObject(value = OpenApiExamples.CONFLICT_ERROR))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ApiErrorResponse.class), examples = @ExampleObject(value = OpenApiExamples.INTERNAL_ERROR)))
    })
    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    private @interface AdminResponses {
        boolean created() default false;
        boolean noContent() default false;
    }
}
