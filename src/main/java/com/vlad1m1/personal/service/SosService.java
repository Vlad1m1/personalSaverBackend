package com.vlad1m1.personal.service;

import com.vlad1m1.personal.dto.SosRequest;
import com.vlad1m1.personal.dto.SosResponse;
import com.vlad1m1.personal.enums.SmsDeliveryStatus;
import com.vlad1m1.personal.enums.SosStatus;
import com.vlad1m1.personal.enums.SosTargetType;
import com.vlad1m1.personal.exception.ApiValidationException;
import com.vlad1m1.personal.exception.ResourceNotFoundException;
import com.vlad1m1.personal.mapper.ApiMapper;
import com.vlad1m1.personal.model.Region;
import com.vlad1m1.personal.model.SosEvent;
import com.vlad1m1.personal.repository.RegionRepository;
import com.vlad1m1.personal.repository.SosEventRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

@Service
public class SosService {
    private static final String DEFAULT_EMERGENCY_PHONE = "112";

    private final SosEventRepository sosEventRepository;
    private final RegionRepository regionRepository;

    public SosService(SosEventRepository sosEventRepository, RegionRepository regionRepository) {
        this.sosEventRepository = sosEventRepository;
        this.regionRepository = regionRepository;
    }

    @Transactional
    public SosResponse create(SosRequest request) {
        Region region = null;
        if (request.regionId() != null) {
            region = regionRepository.findById(request.regionId())
                    .orElseThrow(() -> new ResourceNotFoundException("Регион не найден: " + request.regionId()));
        }

        String targetPhone = resolveTargetPhone(request, region);
        LocalDateTime now = LocalDateTime.now();

        SosEvent event = new SosEvent();
        event.setTargetType(request.targetType());
        event.setRegion(region);
        event.setContactPhone(request.targetType() == SosTargetType.EMERGENCY_CONTACT ? request.contactPhone() : null);
        event.setTargetPhone(targetPhone);
        event.setMessage(request.message());
        event.setLatitude(request.latitude());
        event.setLongitude(request.longitude());
        event.setStatus(SosStatus.SENT);
        event.setSmsDeliveryStatus(SmsDeliveryStatus.SENT);
        event.setSmsProviderMessage("Принято SMS-провайдером");
        event.setCreatedAt(now);
        event.setUpdatedAt(now);

        return ApiMapper.toSosResponse(sosEventRepository.save(event));
    }

    @Transactional(readOnly = true)
    public SosResponse getById(UUID id) {
        return sosEventRepository.findById(id)
                .map(ApiMapper::toSosResponse)
                .orElseThrow(() -> new ResourceNotFoundException("SOS-событие не найдено: " + id));
    }

    private String resolveTargetPhone(SosRequest request, Region region) {
        if (request.targetType() == SosTargetType.EMERGENCY_CONTACT) {
            if (request.contactPhone() == null || request.contactPhone().isBlank()) {
                throw new ApiValidationException(
                        "Ошибка валидации",
                        Map.of("contactPhone", "contactPhone обязателен для EMERGENCY_CONTACT")
                );
            }
            return request.contactPhone();
        }

        if (region == null || region.getEmergencyPhone() == null || region.getEmergencyPhone().isBlank()) {
            return DEFAULT_EMERGENCY_PHONE;
        }
        return region.getEmergencyPhone();
    }
}
