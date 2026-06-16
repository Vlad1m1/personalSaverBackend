package com.vlad1m1.personal.service;

import com.vlad1m1.personal.dto.RegionRequest;
import com.vlad1m1.personal.dto.RegionResponse;
import com.vlad1m1.personal.exception.ApiConflictException;
import com.vlad1m1.personal.exception.ResourceNotFoundException;
import com.vlad1m1.personal.mapper.ApiMapper;
import com.vlad1m1.personal.model.Region;
import com.vlad1m1.personal.repository.RegionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class RegionService {
    private final RegionRepository regionRepository;

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
                .orElseThrow(() -> new ResourceNotFoundException("Region not found: " + id));
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
    public RegionResponse createRegion(RegionRequest request) {
        String regionName = request.name().trim();
        if (regionRepository.existsByRegionName(regionName)) {
            throw new ApiConflictException("Region already exists: " + regionName);
        }
        Region region = new Region();
        region.setRegionName(regionName);
        region.setEmergencyPhone(blankToNull(request.emergencyPhone()));
        return ApiMapper.toRegionResponse(regionRepository.save(region));
    }

    @Transactional
    public Region createRegion(Region region) {
        return regionRepository.save(region);
    }

    @Transactional
    public RegionResponse updateRegion(Long id, RegionRequest request) {
        Region region = regionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Region not found: " + id));
        String regionName = request.name().trim();
        if (regionRepository.existsByRegionNameAndIdNot(regionName, id)) {
            throw new ApiConflictException("Region already exists: " + regionName);
        }
        region.setRegionName(regionName);
        region.setEmergencyPhone(blankToNull(request.emergencyPhone()));
        return ApiMapper.toRegionResponse(regionRepository.save(region));
    }

    @Transactional
    public void deleteRegion(Long id) {
        if (!regionRepository.existsById(id)) {
            throw new ResourceNotFoundException("Region not found: " + id);
        }
        regionRepository.deleteById(id);
    }

    private String blankToNull(String value) {
        return value == null || value.isBlank() ? null : value.trim();
    }
}
