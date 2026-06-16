package com.vlad1m1.personal.repository;

import com.vlad1m1.personal.model.Region;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RegionRepository extends JpaRepository<Region, Long> {
    boolean existsByRegionName(String regionName);

    boolean existsByRegionNameAndIdNot(String regionName, Long id);
}
