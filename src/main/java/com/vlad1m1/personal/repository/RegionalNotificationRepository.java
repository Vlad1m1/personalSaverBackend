package com.vlad1m1.personal.repository;

import com.vlad1m1.personal.model.RegionalNotification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface RegionalNotificationRepository extends JpaRepository<RegionalNotification, UUID> {
    List<RegionalNotification> findByRegion_IdAndActiveTrueOrderByPublishedAtDesc(Long regionId);

    List<RegionalNotification> findTop5ByRegion_IdAndActiveTrueOrderByPublishedAtDesc(Long regionId);
}
