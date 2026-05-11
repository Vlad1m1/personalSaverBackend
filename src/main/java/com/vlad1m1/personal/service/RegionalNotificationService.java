package com.vlad1m1.personal.service;

import com.vlad1m1.personal.dto.RegionalNotificationResponse;
import com.vlad1m1.personal.exception.ResourceNotFoundException;
import com.vlad1m1.personal.mapper.ApiMapper;
import com.vlad1m1.personal.repository.RegionRepository;
import com.vlad1m1.personal.repository.RegionalNotificationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class RegionalNotificationService {
    private final RegionalNotificationRepository regionalNotificationRepository;
    private final RegionRepository regionRepository;

    public RegionalNotificationService(RegionalNotificationRepository regionalNotificationRepository, RegionRepository regionRepository) {
        this.regionalNotificationRepository = regionalNotificationRepository;
        this.regionRepository = regionRepository;
    }

    @Transactional(readOnly = true)
    public List<RegionalNotificationResponse> getByRegion(Long regionId) {
        validateRegion(regionId);
        return regionalNotificationRepository.findByRegion_IdAndActiveTrueOrderByPublishedAtDesc(regionId).stream()
                .map(ApiMapper::toRegionalNotificationResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<RegionalNotificationResponse> getLatestByRegion(Long regionId) {
        validateRegion(regionId);
        return regionalNotificationRepository.findTop5ByRegion_IdAndActiveTrueOrderByPublishedAtDesc(regionId).stream()
                .map(ApiMapper::toRegionalNotificationResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public RegionalNotificationResponse getById(UUID id) {
        return regionalNotificationRepository.findById(id)
                .map(ApiMapper::toRegionalNotificationResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Уведомление не найдено: " + id));
    }

    private void validateRegion(Long regionId) {
        if (!regionRepository.existsById(regionId)) {
            throw new ResourceNotFoundException("Регион не найден: " + regionId);
        }
    }
}
