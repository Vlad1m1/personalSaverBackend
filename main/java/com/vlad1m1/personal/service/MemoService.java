package com.vlad1m1.personal.service;

import com.vlad1m1.personal.model.Memo;
import com.vlad1m1.personal.repository.MemoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class MemoService {

    private final MemoRepository memoRepository;

    @Autowired
    public MemoService(MemoRepository memoRepository) {
        this.memoRepository = memoRepository;
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
                .orElseThrow(() -> new RuntimeException("Memo not found with id " + id));

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
}
