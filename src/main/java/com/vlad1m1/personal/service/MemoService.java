package com.vlad1m1.personal.service;

import com.vlad1m1.personal.dto.MemoDetailResponse;
import com.vlad1m1.personal.dto.MemoRequest;
import com.vlad1m1.personal.dto.MemoSummaryResponse;
import com.vlad1m1.personal.dto.MemoUpdatesResponse;
import com.vlad1m1.personal.exception.ApiValidationException;
import com.vlad1m1.personal.exception.ResourceNotFoundException;
import com.vlad1m1.personal.mapper.ApiMapper;
import com.vlad1m1.personal.model.Category;
import com.vlad1m1.personal.model.Memo;
import com.vlad1m1.personal.model.Region;
import com.vlad1m1.personal.repository.CategoryRepository;
import com.vlad1m1.personal.repository.MemoRepository;
import com.vlad1m1.personal.repository.RegionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
public class MemoService {
    private final MemoRepository memoRepository;
    private final RegionRepository regionRepository;
    private final CategoryRepository categoryRepository;

    public MemoService(MemoRepository memoRepository, RegionRepository regionRepository, CategoryRepository categoryRepository) {
        this.memoRepository = memoRepository;
        this.regionRepository = regionRepository;
        this.categoryRepository = categoryRepository;
    }

    @Transactional(readOnly = true)
    public List<MemoSummaryResponse> getMemoSummaries(Long regionId) {
        return getMemoSummaries(regionId, null, null, null);
    }

    @Transactional(readOnly = true)
    public List<MemoSummaryResponse> getMemoSummaries(Long regionId, Long categoryId, Boolean active, Boolean critical) {
        validateRegionIfPresent(regionId);
        validateCategoryIfPresent(categoryId);
        Boolean activeFilter = active == null ? Boolean.TRUE : active;
        return memoRepository.findFilteredPublicMemos(regionId, categoryId, activeFilter, critical).stream()
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
                .orElseThrow(() -> new ResourceNotFoundException("Memo not found: " + id));
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
    public MemoDetailResponse createMemo(MemoRequest request) {
        Memo memo = new Memo();
        LocalDateTime now = LocalDateTime.now();
        memo.setCreatedAt(now);
        memo.setUpdatedAt(now);
        apply(memo, request);
        if (request.active() == null) {
            memo.setActive(true);
        }
        return ApiMapper.toMemoDetailResponse(memoRepository.save(memo));
    }

    @Transactional
    public Memo createMemo(Memo memo) {
        memo.setCreatedAt(LocalDateTime.now());
        memo.setUpdatedAt(LocalDateTime.now());
        return memoRepository.save(memo);
    }

    @Transactional
    public MemoDetailResponse updateMemo(UUID id, MemoRequest request) {
        Memo memo = memoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Memo not found: " + id));
        apply(memo, request);
        memo.setUpdatedAt(LocalDateTime.now());
        return ApiMapper.toMemoDetailResponse(memoRepository.save(memo));
    }

    @Transactional
    public void deleteMemo(UUID id) {
        if (!memoRepository.existsById(id)) {
            throw new ResourceNotFoundException("Memo not found: " + id);
        }
        memoRepository.deleteById(id);
    }

    @Transactional
    public MemoDetailResponse publishMemo(UUID id) {
        Memo memo = memoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Memo not found: " + id));
        memo.setActive(true);
        memo.setUpdatedAt(LocalDateTime.now());
        return ApiMapper.toMemoDetailResponse(memoRepository.save(memo));
    }

    @Transactional
    public MemoDetailResponse unpublishMemo(UUID id) {
        Memo memo = memoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Memo not found: " + id));
        memo.setActive(false);
        memo.setUpdatedAt(LocalDateTime.now());
        return ApiMapper.toMemoDetailResponse(memoRepository.save(memo));
    }

    private void apply(Memo memo, MemoRequest request) {
        Category category = request.categoryId() == null ? null : categoryRepository.findById(request.categoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found: " + request.categoryId()));
        Region region = request.regionId() == null ? null : regionRepository.findById(request.regionId())
                .orElseThrow(() -> new ResourceNotFoundException("Region not found: " + request.regionId()));

        memo.setCategory(category);
        memo.setRegion(region);
        memo.setTitle(requiredTrim(request.title(), "title"));
        memo.setShortDescription(requiredTrim(request.shortDescription(), "shortDescription"));
        memo.setContent(requiredTrim(request.htmlContent(), "htmlContent"));
        memo.setSteps(request.steps() == null ? new ArrayList<>() : new ArrayList<>(request.steps()));
        memo.setVersion(request.version() == null ? 1 : request.version());
        memo.setCritical(Boolean.TRUE.equals(request.critical()));
        memo.setImageUrl(blankToNull(request.imageUrl()));
        memo.setIconName(blankToNull(request.iconName()));
        memo.setAccentColor(blankToNull(request.accentColor()));
        if (request.active() != null) {
            memo.setActive(request.active());
        }
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
            throw new ResourceNotFoundException("Region not found: " + regionId);
        }
    }

    private void validateCategoryIfPresent(Long categoryId) {
        if (categoryId != null && !categoryRepository.existsById(categoryId)) {
            throw new ResourceNotFoundException("Category not found: " + categoryId);
        }
    }

    private String requiredTrim(String value, String field) {
        if (value == null || value.isBlank()) {
            throw new ApiValidationException("Validation error", Map.of(field, field + " is required"));
        }
        return value.trim();
    }

    private String blankToNull(String value) {
        return value == null || value.isBlank() ? null : value.trim();
    }
}
