package com.vlad1m1.personal.service;

import com.vlad1m1.personal.model.Alarm;
import com.vlad1m1.personal.repository.AlarmRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class AlarmService {

    private final AlarmRepository alarmRepository;

    @Autowired
    public AlarmService(AlarmRepository alarmRepository) {
        this.alarmRepository = alarmRepository;
    }

    @Transactional(readOnly = true)
    public List<Alarm> getAllAlarms() {
        return alarmRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Alarm> getAlarmById(UUID id) {
        return alarmRepository.findById(id);
    }

    @Transactional
    public Alarm createAlarm(Alarm alarm) {
        return alarmRepository.save(alarm);
    }

    @Transactional
    public Alarm updateAlarm(UUID id, Alarm alarmDetails) {
        Alarm alarm = alarmRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Тревога не найдена: " + id));
        alarm.setText(alarmDetails.getText());
        alarm.setRegion(alarmDetails.getRegion());
        return alarmRepository.save(alarm);
    }

    @Transactional
    public void deleteAlarm(UUID id) {
        alarmRepository.deleteById(id);
    }
}
