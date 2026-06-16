package com.vlad1m1.personal.controller;

import com.vlad1m1.personal.config.OpenApiExamples;
import com.vlad1m1.personal.dto.ApiErrorResponse;
import com.vlad1m1.personal.dto.MemoCategoryResponse;
import com.vlad1m1.personal.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
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

    @Operation(summary = "List memo categories", description = "Returns memo categories used by the Flutter catalog UI for grouping published memos.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Categories returned",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = MemoCategoryResponse.class),
                            examples = @ExampleObject(name = "Categories", value = OpenApiExamples.CATEGORIES_RESPONSE))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class), examples = @ExampleObject(value = OpenApiExamples.INTERNAL_ERROR)))
    })
    @GetMapping
    public List<MemoCategoryResponse> getAllCategories() {
        return categoryService.getAllCategoryResponses();
    }
}
