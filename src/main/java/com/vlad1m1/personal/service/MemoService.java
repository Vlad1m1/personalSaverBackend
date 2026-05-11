package com.vlad1m1.personal.service;

import com.vlad1m1.personal.dto.MemoDetailResponse;
import com.vlad1m1.personal.dto.MemoSummaryResponse;
import com.vlad1m1.personal.dto.MemoUpdatesResponse;
import com.vlad1m1.personal.exception.ResourceNotFoundException;
import com.vlad1m1.personal.mapper.ApiMapper;
import com.vlad1m1.personal.model.Memo;
import com.vlad1m1.personal.repository.MemoRepository;
import com.vlad1m1.personal.repository.RegionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class MemoService {

    private final MemoRepository memoRepository;
    private final RegionRepository regionRepository;

    @Autowired
    public MemoService(MemoRepository memoRepository, RegionRepository regionRepository) {
        this.memoRepository = memoRepository;
        this.regionRepository = regionRepository;
    }

    @Transactional(readOnly = true)
    public List<MemoSummaryResponse> getMemoSummaries(Long regionId) {
        validateRegionIfPresent(regionId);
        return loadMemos(regionId, null).stream()
                .map(ApiMapper::toMemoSummaryResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public MemoUpdatesResponse getMemoUpdates(LocalDateTime since, Long regionId) {
        validateRegionIfPresent(regionId);
        return new MemoUpdatesResponse(
                OffsetDateTime.now(),
                loadMemos(regionId, since).stream()
                        .map(ApiMapper::toMemoSummaryResponse)
                        .toList()
        );
    }

    @Transactional(readOnly = true)
    public MemoDetailResponse getMemoDetail(UUID id) {
        return memoRepository.findById(id)
                .map(ApiMapper::toMemoDetailResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Памятка не найдена: " + id));
    }

    @Transactional(readOnly = true)
    public List<Memo> getAllMemos() {
        return memoRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Memo> getMemoById(UUID id) {
        return memoRepository.findById(id);
    }

    @Transactional
    public Memo createMemo(Memo memo) {
        memo.setCreatedAt(LocalDateTime.now());
        memo.setUpdatedAt(LocalDateTime.now());
        return memoRepository.save(memo);
    }

    @Transactional
    public Memo updateMemo(UUID id, Memo memoDetails) {
        Memo memo = memoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Памятка не найдена: " + id));

        memo.setCategory(memoDetails.getCategory());
        memo.setTitle(memoDetails.getTitle());
        memo.setShortDescription(memoDetails.getShortDescription());
        memo.setContent(memoDetails.getContent());
        memo.setVersion(memoDetails.getVersion());
        memo.setCritical(memoDetails.isCritical());
        memo.setImageUrl(memoDetails.getImageUrl());
        memo.setActive(memoDetails.isActive());
        memo.setUpdatedAt(LocalDateTime.now());

        return memoRepository.save(memo);
    }

    @Transactional
    public void deleteMemo(UUID id) {
        memoRepository.deleteById(id);
    }

    private List<Memo> loadMemos(Long regionId, LocalDateTime since) {
        List<Memo> result = new ArrayList<>();
        if (since == null) {
            result.addAll(memoRepository.findActiveGeneralMemos());
            if (regionId != null) {
                result.addAll(memoRepository.findActiveRegionalMemos(regionId));
            }
            return result;
        }

        result.addAll(memoRepository.findUpdatedGeneralMemos(since));
        if (regionId != null) {
            result.addAll(memoRepository.findUpdatedRegionalMemos(since, regionId));
        }
        return result;
    }

    private void validateRegionIfPresent(Long regionId) {
        if (regionId != null && !regionRepository.existsById(regionId)) {
            throw new ResourceNotFoundException("Регион не найден: " + regionId);
        }
    }
}
