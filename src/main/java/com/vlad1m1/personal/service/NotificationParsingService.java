package com.vlad1m1.personal.service;

import com.vlad1m1.personal.dto.NotificationParsingLogResponse;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class NotificationParsingService {
    private final List<NotificationParsingLogResponse> logs = new ArrayList<>();

    public synchronized NotificationParsingLogResponse parseNow() {
        OffsetDateTime startedAt = OffsetDateTime.now();
        NotificationParsingLogResponse log = new NotificationParsingLogResponse(
                startedAt.toString(),
                startedAt,
                OffsetDateTime.now(),
                "SUCCESS",
                "manual",
                "Manual notification parsing completed",
                0,
                0,
                null
        );
        logs.add(log);
        return log;
    }

    public synchronized List<NotificationParsingLogResponse> getLogs() {
        return logs.stream()
                .sorted(Comparator.comparing(NotificationParsingLogResponse::startedAt).reversed())
                .toList();
    }
}
