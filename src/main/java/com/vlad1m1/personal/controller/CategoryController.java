package com.vlad1m1.personal.controller;

import com.vlad1m1.personal.config.OpenApiExamples;
import com.vlad1m1.personal.dto.ApiErrorResponse;
import com.vlad1m1.personal.dto.MemoCategoryResponse;
import com.vlad1m1.personal.service.CategoryService;
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
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/memo-categories")
@Tag(name = "Public Memos")
public class CategoryController {
    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @Operation(summary = "Получить категории памяток", description = "Возвращает категории, которые Flutter-каталог использует для группировки опубликованных памяток. Список отсортирован по displayOrder.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Категории возвращены",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = MemoCategoryResponse.class),
                            examples = @ExampleObject(name = "Categories", value = OpenApiExamples.CATEGORIES_RESPONSE))),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class), examples = @ExampleObject(value = OpenApiExamples.INTERNAL_ERROR)))
    })
    @GetMapping
    public List<MemoCategoryResponse> getAllCategories() {
        return categoryService.getAllCategoryResponses();
    }

    @Operation(summary = "Получить категорию памяток", description = "Возвращает одну категорию памяток по id.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Категория возвращена",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = MemoCategoryResponse.class),
                            examples = @ExampleObject(name = "Category", value = OpenApiExamples.CATEGORY_RESPONSE))),
            @ApiResponse(responseCode = "404", description = "Категория не найдена",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class), examples = @ExampleObject(value = OpenApiExamples.NOT_FOUND_ERROR))),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class), examples = @ExampleObject(value = OpenApiExamples.INTERNAL_ERROR)))
    })
    @GetMapping("/{id}")
    public MemoCategoryResponse getCategoryById(
            @Parameter(description = "Идентификатор категории.", example = "1", required = true)
            @PathVariable Long id
    ) {
        return categoryService.getCategoryResponseById(id);
    }
}
