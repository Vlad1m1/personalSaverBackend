package com.vlad1m1.personal.controller;

import com.vlad1m1.personal.config.OpenApiExamples;
import com.vlad1m1.personal.dto.ApiErrorResponse;
import com.vlad1m1.personal.dto.RegionResponse;
import com.vlad1m1.personal.service.RegionService;
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
@RequestMapping("/api/regions")
@Tag(name = "Public Regions")
public class RegionController {
    private final RegionService regionService;

    public RegionController(RegionService regionService) {
        this.regionService = regionService;
    }

    @Operation(summary = "List public regions", description = "Returns regions available to the mobile app for memo filtering, notification filtering, and SOS emergency-service routing.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Regions returned",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = RegionResponse.class),
                            examples = @ExampleObject(name = "Regions", value = OpenApiExamples.REGIONS_RESPONSE))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class), examples = @ExampleObject(value = OpenApiExamples.INTERNAL_ERROR)))
    })
    @GetMapping
    public List<RegionResponse> getAllRegions() {
        return regionService.getAllRegionResponses();
    }

    @Operation(summary = "Get public region", description = "Returns a single region by id for mobile diagnostics and local cache repair.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Region returned",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = RegionResponse.class),
                            examples = @ExampleObject(name = "Region", value = OpenApiExamples.REGION_RESPONSE))),
            @ApiResponse(responseCode = "404", description = "Not Found",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class), examples = @ExampleObject(value = OpenApiExamples.NOT_FOUND_ERROR))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class), examples = @ExampleObject(value = OpenApiExamples.INTERNAL_ERROR)))
    })
    @GetMapping("/{id}")
    public RegionResponse getRegionById(
            @Parameter(description = "Region id.", example = "1", required = true)
            @PathVariable Long id
    ) {
        return regionService.getRegionResponseById(id);
    }
}
