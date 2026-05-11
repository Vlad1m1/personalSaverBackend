package com.vlad1m1.personal.mapper;

import com.vlad1m1.personal.dto.MemoCategoryResponse;
import com.vlad1m1.personal.dto.MemoDetailResponse;
import com.vlad1m1.personal.dto.MemoSummaryResponse;
import com.vlad1m1.personal.dto.RegionResponse;
import com.vlad1m1.personal.dto.RegionalNotificationResponse;
import com.vlad1m1.personal.dto.SmsDeliveryResponse;
import com.vlad1m1.personal.dto.SosResponse;
import com.vlad1m1.personal.model.Category;
import com.vlad1m1.personal.model.Memo;
import com.vlad1m1.personal.model.Region;
import com.vlad1m1.personal.model.RegionalNotification;
import com.vlad1m1.personal.model.SosEvent;

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
                memo.getShortDescription(),
                memo.getVersion(),
                memo.isCritical(),
                memo.getImageUrl(),
                memo.getUpdatedAt()
        );
    }

    public static MemoDetailResponse toMemoDetailResponse(Memo memo) {
        return new MemoDetailResponse(
                memo.getId(),
                memo.getCategory() == null ? null : memo.getCategory().getId(),
                memo.getRegion() == null ? null : memo.getRegion().getId(),
                memo.getTitle(),
                memo.getShortDescription(),
                memo.getContent(),
                memo.getSteps(),
                memo.getVersion(),
                memo.isCritical(),
                memo.getImageUrl(),
                memo.getUpdatedAt()
        );
    }

    public static RegionalNotificationResponse toRegionalNotificationResponse(RegionalNotification notification) {
        return new RegionalNotificationResponse(
                notification.getId(),
                notification.getRegion().getId(),
                notification.getTitle(),
                notification.getMessage(),
                notification.getSeverity(),
                notification.getPublishedAt()
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
                new SmsDeliveryResponse(event.getTargetPhone(), event.getSmsDeliveryStatus(), event.getSmsProviderMessage()),
                event.getCreatedAt(),
                event.getUpdatedAt()
        );
    }

    private ApiMapper() {
    }
}
