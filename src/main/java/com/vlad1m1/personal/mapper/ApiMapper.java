package com.vlad1m1.personal.mapper;

import com.vlad1m1.personal.dto.AlarmResponse;
import com.vlad1m1.personal.dto.MemoCategoryResponse;
import com.vlad1m1.personal.dto.MemoDetailResponse;
import com.vlad1m1.personal.dto.MemoSummaryResponse;
import com.vlad1m1.personal.dto.RegionResponse;
import com.vlad1m1.personal.dto.RegionalNotificationResponse;
import com.vlad1m1.personal.dto.SmsDeliveryResponse;
import com.vlad1m1.personal.dto.SosResponse;
import com.vlad1m1.personal.model.Alarm;
import com.vlad1m1.personal.model.Category;
import com.vlad1m1.personal.model.Memo;
import com.vlad1m1.personal.model.Region;
import com.vlad1m1.personal.model.RegionalNotification;
import com.vlad1m1.personal.model.SosEvent;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.HexFormat;
import java.util.Locale;

public final class ApiMapper {

    public static RegionResponse toRegionResponse(Region region) {
        return new RegionResponse(
                region.getId(),
                region.getRegionName(),
                region.getEmergencyPhone()
        );
    }

    public static MemoCategoryResponse toMemoCategoryResponse(Category category) {
        return new MemoCategoryResponse(
                category.getId(),
                category.getName(),
                category.getIconName(),
                category.getAccentColor(),
                category.getDisplayOrder(),
                category.getUpdatedAt()
        );
    }

    public static MemoSummaryResponse toMemoSummaryResponse(Memo memo) {
        return new MemoSummaryResponse(
                memo.getId(),
                memo.getCategory() == null ? null : memo.getCategory().getId(),
                memo.getRegion() == null ? null : memo.getRegion().getId(),
                memo.getTitle(),
                slug(memo.getTitle()),
                memo.getShortDescription(),
                contentHash(memo),
                memo.getVersion(),
                memo.getUpdatedAt()
        );
    }

    public static MemoDetailResponse toMemoDetailResponse(Memo memo) {
        return new MemoDetailResponse(
                memo.getId(),
                memo.getCategory() == null ? null : memo.getCategory().getId(),
                memo.getRegion() == null ? null : memo.getRegion().getId(),
                memo.getTitle(),
                slug(memo.getTitle()),
                memo.getShortDescription(),
                memo.getContent(),
                memo.getSteps(),
                memo.getIconName(),
                memo.getAccentColor(),
                contentHash(memo),
                memo.getVersion(),
                memo.isCritical(),
                memo.getImageUrl(),
                memo.getUpdatedAt()
        );
    }

    public static RegionalNotificationResponse toRegionalNotificationResponse(RegionalNotification notification) {
        LocalDateTime receivedAt = notification.getReceivedAt() == null
                ? notification.getPublishedAt()
                : notification.getReceivedAt();
        return new RegionalNotificationResponse(
                notification.getId(),
                notification.getRegion().getId(),
                notification.getTitle(),
                notification.getMessage(),
                notification.getSeverity(),
                notification.getPublishedAt(),
                receivedAt
        );
    }

    public static SosResponse toSosResponse(SosEvent event) {
        return new SosResponse(
                event.getId(),
                event.getTargetType(),
                event.getRegion() == null ? null : event.getRegion().getId(),
                event.getStatus(),
                event.getTargetPhone(),
                event.getMessage(),
                event.getLatitude(),
                event.getLongitude(),
                event.getAccuracyMeters(),
                event.getAddress(),
                new SmsDeliveryResponse(event.getTargetPhone(), event.getSmsDeliveryStatus(), event.getSmsProviderMessage()),
                event.getCreatedAt(),
                event.getUpdatedAt()
        );
    }

    public static AlarmResponse toAlarmResponse(Alarm alarm) {
        return new AlarmResponse(
                alarm.getId(),
                alarm.getRegion().getId(),
                alarm.getText()
        );
    }

    private static String slug(String value) {
        if (value == null || value.isBlank()) {
            return "memo";
        }
        String slug = value.toLowerCase(Locale.ROOT)
                .replaceAll("[^a-z0-9]+", "-")
                .replaceAll("(^-|-$)", "");
        return slug.isBlank() ? "memo" : slug;
    }

    private static String contentHash(Memo memo) {
        String source = String.join("|",
                nullToEmpty(memo.getTitle()),
                nullToEmpty(memo.getShortDescription()),
                nullToEmpty(memo.getContent()),
                String.valueOf(memo.getVersion()),
                String.valueOf(memo.getUpdatedAt()));
        try {
            byte[] digest = MessageDigest.getInstance("SHA-256").digest(source.getBytes(StandardCharsets.UTF_8));
            return "sha256:" + HexFormat.of().formatHex(digest).substring(0, 12);
        } catch (NoSuchAlgorithmException exception) {
            throw new IllegalStateException("SHA-256 is not available", exception);
        }
    }

    private static String nullToEmpty(String value) {
        return value == null ? "" : value;
    }

    private ApiMapper() {
    }
}
