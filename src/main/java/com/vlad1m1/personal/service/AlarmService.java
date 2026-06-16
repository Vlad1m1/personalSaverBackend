package com.vlad1m1.personal.service;

import com.vlad1m1.personal.dto.AlarmRequest;
import com.vlad1m1.personal.dto.AlarmResponse;
import com.vlad1m1.personal.exception.ResourceNotFoundException;
import com.vlad1m1.personal.mapper.ApiMapper;
import com.vlad1m1.personal.model.Alarm;
import com.vlad1m1.personal.model.Region;
import com.vlad1m1.personal.repository.AlarmRepository;
import com.vlad1m1.personal.repository.RegionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class AlarmService {

    private final AlarmRepository alarmRepository;
    private final RegionRepository regionRepository;

    public AlarmService(AlarmRepository alarmRepository, RegionRepository regionRepository) {
        this.alarmRepository = alarmRepository;
        this.regionRepository = regionRepository;
    }

    @Transactional(readOnly = true)
    public List<AlarmResponse> getAllAlarms(Long regionId) {
        validateRegionIfPresent(regionId);
        List<Alarm> alarms = regionId == null ? alarmRepository.findAll() : alarmRepository.findByRegionId(regionId);
        return alarms.stream()
                .map(ApiMapper::toAlarmResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public AlarmResponse getAlarmById(UUID id) {
        return alarmRepository.findById(id)
                .map(ApiMapper::toAlarmResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Alarm not found: " + id));
    }

    @Transactional
    public AlarmResponse createAlarm(AlarmRequest request) {
        Alarm alarm = new Alarm();
        apply(alarm, request);
        return ApiMapper.toAlarmResponse(alarmRepository.save(alarm));
    }

    @Transactional
    public AlarmResponse updateAlarm(UUID id, AlarmRequest request) {
        Alarm alarm = alarmRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Alarm not found: " + id));
        apply(alarm, request);
        return ApiMapper.toAlarmResponse(alarmRepository.save(alarm));
    }

    @Transactional
    public void deleteAlarm(UUID id) {
        if (!alarmRepository.existsById(id)) {
            throw new ResourceNotFoundException("Alarm not found: " + id);
        }
        alarmRepository.deleteById(id);
    }

    private void apply(Alarm alarm, AlarmRequest request) {
        Region region = regionRepository.findById(request.regionId())
                .orElseThrow(() -> new ResourceNotFoundException("Region not found: " + request.regionId()));
        alarm.setRegion(region);
        alarm.setText(request.text().trim());
    }

    private void validateRegionIfPresent(Long regionId) {
        if (regionId != null && !regionRepository.existsById(regionId)) {
            throw new ResourceNotFoundException("Region not found: " + regionId);
        }
    }
}
