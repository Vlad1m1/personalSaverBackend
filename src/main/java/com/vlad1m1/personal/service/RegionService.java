package com.vlad1m1.personal.service;

import com.vlad1m1.personal.dto.RegionResponse;
import com.vlad1m1.personal.exception.ResourceNotFoundException;
import com.vlad1m1.personal.mapper.ApiMapper;
import com.vlad1m1.personal.model.Region;
import com.vlad1m1.personal.repository.RegionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class RegionService {

    private final RegionRepository regionRepository;

    @Autowired
    public RegionService(RegionRepository regionRepository) {
        this.regionRepository = regionRepository;
    }

    @Transactional(readOnly = true)
    public List<RegionResponse> getAllRegionResponses() {
        return regionRepository.findAll().stream()
                .map(ApiMapper::toRegionResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public RegionResponse getRegionResponseById(Long id) {
        return regionRepository.findById(id)
                .map(ApiMapper::toRegionResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Регион не найден: " + id));
    }

    @Transactional(readOnly = true)
    public List<Region> getAllRegions() {
        return regionRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Region> getRegionById(Long id) {
        return regionRepository.findById(id);
    }

    @Transactional
    public Region createRegion(Region region) {
        return regionRepository.save(region);
    }

    @Transactional
    public Region updateRegion(Long id, Region regionDetails) {
        Region region = regionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Регион не найден: " + id));
        region.setRegionName(regionDetails.getRegionName());
        return regionRepository.save(region);
    }

    @Transactional
    public void deleteRegion(Long id) {
        regionRepository.deleteById(id);
    }
}
