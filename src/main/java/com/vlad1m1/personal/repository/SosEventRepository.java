package com.vlad1m1.personal.repository;

import com.vlad1m1.personal.model.SosEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface SosEventRepository extends JpaRepository<SosEvent, UUID> {
    List<SosEvent> findAllByOrderByCreatedAtDesc();

    List<SosEvent> findByRegionIdOrderByCreatedAtDesc(Long regionId);
}
