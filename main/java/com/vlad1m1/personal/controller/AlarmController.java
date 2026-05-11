package com.vlad1m1.personal.controller;

import com.vlad1m1.personal.model.Alarm;
import com.vlad1m1.personal.service.AlarmService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/alarms")
@Tag(name = "Alarms", description = "API для управления тревогами")
public class AlarmController {

    private final AlarmService alarmService;

    @Autowired
    public AlarmController(AlarmService alarmService) {
        this.alarmService = alarmService;
    }

    @Operation(summary = "Получить список всех тревог")
    @GetMapping
    public List<Alarm> getAllAlarms() {
        return alarmService.getAllAlarms();
    }

    @Operation(summary = "Получить тревогу по ID")
    @GetMapping("/{id}")
    public ResponseEntity<Alarm> getAlarmById(@PathVariable UUID id) {
        return alarmService.getAlarmById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Создать новую тревогу")
    @PostMapping
    public Alarm createAlarm(@RequestBody Alarm alarm) {
        return alarmService.createAlarm(alarm);
    }

    @Operation(summary = "Обновить существующую тревогу")
    @PutMapping("/{id}")
    public ResponseEntity<Alarm> updateAlarm(@PathVariable UUID id, @RequestBody Alarm alarmDetails) {
        try {
            Alarm updatedAlarm = alarmService.updateAlarm(id, alarmDetails);
            return ResponseEntity.ok(updatedAlarm);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Удалить тревогу")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAlarm(@PathVariable UUID id) {
        alarmService.deleteAlarm(id);
        return ResponseEntity.noContent().build();
    }
}
