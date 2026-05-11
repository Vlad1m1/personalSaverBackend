package com.vlad1m1.personal.repository;

import com.vlad1m1.personal.model.Memo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface MemoRepository extends JpaRepository<Memo, UUID> {
}
