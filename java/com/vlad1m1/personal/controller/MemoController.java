package com.vlad1m1.personal.controller;

import com.vlad1m1.personal.model.Memo;
import com.vlad1m1.personal.service.MemoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/memos")
@Tag(name = "Memos", description = "API для управления памятками")
public class MemoController {

    private final MemoService memoService;

    @Autowired
    public MemoController(MemoService memoService) {
        this.memoService = memoService;
    }

    @Operation(summary = "Получить список всех памяток")
    @GetMapping
    public List<Memo> getAllMemos() {
        return memoService.getAllMemos();
    }

    @Operation(summary = "Получить памятку по ID")
    @GetMapping("/{id}")
    public ResponseEntity<Memo> getMemoById(@PathVariable UUID id) {
        return memoService.getMemoById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Создать новую памятку")
    @PostMapping
    public Memo createMemo(@RequestBody Memo memo) {
        return memoService.createMemo(memo);
    }

    @Operation(summary = "Обновить существующую памятку")
    @PutMapping("/{id}")
    public ResponseEntity<Memo> updateMemo(@PathVariable UUID id, @RequestBody Memo memoDetails) {
        try {
            Memo updatedMemo = memoService.updateMemo(id, memoDetails);
            return ResponseEntity.ok(updatedMemo);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Удалить памятку")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMemo(@PathVariable UUID id) {
        memoService.deleteMemo(id);
        return ResponseEntity.noContent().build();
    }
}
