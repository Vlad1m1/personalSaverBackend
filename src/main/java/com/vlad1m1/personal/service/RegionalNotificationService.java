package com.vlad1m1.personal.service;

import com.vlad1m1.personal.dto.RegionalNotificationRequest;
import com.vlad1m1.personal.dto.RegionalNotificationResponse;
import com.vlad1m1.personal.enums.NotificationSeverity;
import com.vlad1m1.personal.exception.ResourceNotFoundException;
import com.vlad1m1.personal.mapper.ApiMapper;
import com.vlad1m1.personal.model.Region;
import com.vlad1m1.personal.model.RegionalNotification;
import com.vlad1m1.personal.repository.RegionRepository;
import com.vlad1m1.personal.repository.RegionalNotificationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
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
    public List<RegionalNotificationResponse> getByRegion(Long regionId, NotificationSeverity severity, Integer limit) {
        validateRegion(regionId);
        int effectiveLimit = limit == null ? 50 : Math.min(Math.max(limit, 1), 100);
        return regionalNotificationRepository.findByRegion_IdAndActiveTrueOrderByPublishedAtDesc(regionId).stream()
                .filter(notification -> severity == null || notification.getSeverity() == severity)
                .limit(effectiveLimit)
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
                .orElseThrow(() -> new ResourceNotFoundException("Notification not found: " + id));
    }

    @Transactional
    public RegionalNotificationResponse create(RegionalNotificationRequest request) {
        RegionalNotification notification = new RegionalNotification();
        apply(notification, request);
        if (notification.getPublishedAt() == null) {
            notification.setPublishedAt(LocalDateTime.now());
        }
        if (notification.getReceivedAt() == null) {
            notification.setReceivedAt(LocalDateTime.now());
        }
        if (request.active() == null) {
            notification.setActive(true);
        }
        return ApiMapper.toRegionalNotificationResponse(regionalNotificationRepository.save(notification));
    }

    @Transactional
    public RegionalNotificationResponse update(UUID id, RegionalNotificationRequest request) {
        RegionalNotification notification = regionalNotificationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Notification not found: " + id));
        apply(notification, request);
        if (notification.getReceivedAt() == null) {
            notification.setReceivedAt(LocalDateTime.now());
        }
        return ApiMapper.toRegionalNotificationResponse(regionalNotificationRepository.save(notification));
    }

    @Transactional
    public void delete(UUID id) {
        if (!regionalNotificationRepository.existsById(id)) {
            throw new ResourceNotFoundException("Notification not found: " + id);
        }
        regionalNotificationRepository.deleteById(id);
    }

    private void apply(RegionalNotification notification, RegionalNotificationRequest request) {
        Region region = regionRepository.findById(request.regionId())
                .orElseThrow(() -> new ResourceNotFoundException("Region not found: " + request.regionId()));
        notification.setRegion(region);
        notification.setTitle(request.title().trim());
        notification.setMessage(request.message().trim());
        notification.setSeverity(request.severity());
        notification.setPublishedAt(request.publishedAt() == null ? LocalDateTime.now() : request.publishedAt());
        if (notification.getReceivedAt() == null) {
            notification.setReceivedAt(LocalDateTime.now());
        }
        if (request.active() != null) {
            notification.setActive(request.active());
        }
    }

    private void validateRegion(Long regionId) {
        if (!regionRepository.existsById(regionId)) {
            throw new ResourceNotFoundException("Region not found: " + regionId);
        }
    }
}
