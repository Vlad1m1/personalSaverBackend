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

    @Operation(summary = "List published memos", description = "Returns active global memos and, when regionId is provided, active regional memos. Results are sorted by updatedAt descending. List items intentionally do not include htmlContent; call GET /api/memos/{id} for WebView HTML.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Memo list returned",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = MemoSummaryResponse.class),
                            examples = @ExampleObject(name = "Memo list", value = OpenApiExamples.MEMOS_RESPONSE))),
            @ApiResponse(responseCode = "404", description = "Region not found",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class), examples = @ExampleObject(value = OpenApiExamples.NOT_FOUND_ERROR))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class), examples = @ExampleObject(value = OpenApiExamples.INTERNAL_ERROR)))
    })
    @GetMapping
    public List<MemoSummaryResponse> getMemos(
            @Parameter(description = "Optional region id. When present, the response includes global memos plus memos for this region.", example = "1")
            @RequestParam(required = false) Long regionId
    ) {
        return memoService.getMemoSummaries(regionId);
    }

    @Operation(summary = "Get memo updates for offline sync", description = "Returns memos changed after since for offline synchronization. Use contentHash, version, and updatedAt to update local cache entries. regionId limits regional memos while still including global memos.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Memo updates returned",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = MemoUpdatesResponse.class),
                            examples = @ExampleObject(name = "Memo updates", value = OpenApiExamples.MEMO_UPDATES_RESPONSE))),
            @ApiResponse(responseCode = "400", description = "Invalid since or regionId",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class), examples = @ExampleObject(value = OpenApiExamples.VALIDATION_ERROR))),
            @ApiResponse(responseCode = "404", description = "Region not found",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class), examples = @ExampleObject(value = OpenApiExamples.NOT_FOUND_ERROR))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class), examples = @ExampleObject(value = OpenApiExamples.INTERNAL_ERROR)))
    })
    @GetMapping("/updates")
    public MemoUpdatesResponse getMemoUpdates(
            @Parameter(description = "Return only memos updated after this timestamp. Omit for a full sync.", example = "2026-05-12T10:00:00")
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime since,
            @Parameter(description = "Optional region id for regional memo sync.", example = "1")
            @RequestParam(required = false) Long regionId
    ) {
        return memoService.getMemoUpdates(since, regionId);
    }

    @Operation(summary = "Get memo details", description = "Returns full memo content including htmlContent for Flutter WebView. ETag and Last-Modified headers are not currently implemented; use contentHash, version, and updatedAt for cache validation.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Memo details returned",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = MemoDetailResponse.class),
                            examples = @ExampleObject(name = "Memo details", value = OpenApiExamples.MEMO_DETAIL_RESPONSE))),
            @ApiResponse(responseCode = "404", description = "Memo not found",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class), examples = @ExampleObject(value = OpenApiExamples.NOT_FOUND_ERROR))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class), examples = @ExampleObject(value = OpenApiExamples.INTERNAL_ERROR)))
    })
    @GetMapping("/{id}")
    public MemoDetailResponse getMemoById(
            @Parameter(description = "Memo id from the list or update feed.", example = "8c7a15f8-2f68-4d8d-8cbb-2b26f1c4b4d3", required = true)
            @PathVariable UUID id
    ) {
        return memoService.getMemoDetail(id);
    }
}
