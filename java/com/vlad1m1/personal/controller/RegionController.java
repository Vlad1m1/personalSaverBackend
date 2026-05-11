package com.vlad1m1.personal.controller;

import com.vlad1m1.personal.model.Region;
import com.vlad1m1.personal.service.RegionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/regions")
@Tag(name = "Regions", description = "API для управления регионами")
public class RegionController {

    private final RegionService regionService;

    @Autowired
    public RegionController(RegionService regionService) {
        this.regionService = regionService;
    }

    @Operation(summary = "Получить список всех регионов")
    @GetMapping
    public List<Region> getAllRegions() {
        return regionService.getAllRegions();
    }

    @Operation(summary = "Получить регион по ID")
    @GetMapping("/{id}")
    public ResponseEntity<Region> getRegionById(@PathVariable Long id) {
        return regionService.getRegionById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Создать новый регион")
    @PostMapping
    public Region createRegion(@RequestBody Region region) {
        return regionService.createRegion(region);
    }

    @Operation(summary = "Обновить существующий регион")
    @PutMapping("/{id}")
    public ResponseEntity<Region> updateRegion(@PathVariable Long id, @RequestBody Region regionDetails) {
        try {
            Region updatedRegion = regionService.updateRegion(id, regionDetails);
            return ResponseEntity.ok(updatedRegion);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Удалить регион")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRegion(@PathVariable Long id) {
        regionService.deleteRegion(id);
        return ResponseEntity.noContent().build();
    }
}
