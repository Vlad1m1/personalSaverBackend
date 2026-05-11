package com.vlad1m1.personal.service;

import com.vlad1m1.personal.dto.SmsDeliveryResponse;
import com.vlad1m1.personal.exception.ResourceNotFoundException;
import com.vlad1m1.personal.model.SosEvent;
import com.vlad1m1.personal.repository.SosEventRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class SmsService {
    private final SosEventRepository sosEventRepository;

    public SmsService(SosEventRepository sosEventRepository) {
        this.sosEventRepository = sosEventRepository;
    }

    @Transactional(readOnly = true)
    public SmsDeliveryResponse getSosSmsDelivery(UUID sosId) {
        SosEvent event = sosEventRepository.findById(sosId)
                .orElseThrow(() -> new ResourceNotFoundException("SOS-событие не найдено: " + sosId));
        return new SmsDeliveryResponse(event.getTargetPhone(), event.getSmsDeliveryStatus(), event.getSmsProviderMessage());
    }
}
