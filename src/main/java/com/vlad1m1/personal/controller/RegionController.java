package com.vlad1m1.personal.controller;

import com.vlad1m1.personal.config.OpenApiExamples;
import com.vlad1m1.personal.dto.ApiErrorResponse;
import com.vlad1m1.personal.dto.RegionResponse;
import com.vlad1m1.personal.service.RegionService;
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
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/regions")
@Tag(name = "Regions")
public class RegionController {

    private final RegionService regionService;

    public RegionController(RegionService regionService) {
        this.regionService = regionService;
    }

    @Operation(
            summary = "Получить список регионов",
            description = """
                    Возвращает регионы, поддерживаемые backend'ом. Flutter-приложение использует id региона,
                    чтобы загружать региональные памятки, региональные уведомления и номер экстренной службы для SOS.
                    """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Список регионов успешно возвращен",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            array = @ArraySchema(schema = @Schema(implementation = RegionResponse.class)),
                            examples = @ExampleObject(name = "Список регионов", value = OpenApiExamples.REGIONS_RESPONSE))),
            @ApiResponse(responseCode = "500", description = "Непредвиденная ошибка сервера",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ApiErrorResponse.class),
                            examples = @ExampleObject(name = "Ошибка сервера", value = OpenApiExamples.INTERNAL_ERROR)))
    })
    @GetMapping
    public List<RegionResponse> getAllRegions() {
        return regionService.getAllRegionResponses();
    }

    @Operation(
            summary = "Получить регион по id",
            description = "Возвращает один регион вместе с emergencyPhone, который SOS использует в режиме EMERGENCY_SERVICE."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Регион успешно возвращен",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = RegionResponse.class),
                            examples = @ExampleObject(name = "Регион", value = OpenApiExamples.REGION_RESPONSE))),
            @ApiResponse(responseCode = "404", description = "Регион не найден",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ApiErrorResponse.class),
                            examples = @ExampleObject(name = "Не найдено", value = OpenApiExamples.NOT_FOUND_ERROR))),
            @ApiResponse(responseCode = "500", description = "Непредвиденная ошибка сервера",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ApiErrorResponse.class),
                            examples = @ExampleObject(name = "Ошибка сервера", value = OpenApiExamples.INTERNAL_ERROR)))
    })
    @GetMapping("/{id}")
    public RegionResponse getRegionById(
            @Parameter(description = "Идентификатор региона из GET /api/regions.", example = "1", required = true)
            @PathVariable Long id
    ) {
        return regionService.getRegionResponseById(id);
    }
}
