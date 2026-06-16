package com.vlad1m1.personal.repository;

import com.vlad1m1.personal.model.Memo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Repository
public interface MemoRepository extends JpaRepository<Memo, UUID> {
    List<Memo> findByTitleIn(Collection<String> titles);

    @Query("select m from Memo m where m.isActive = true and m.region is null order by m.updatedAt desc")
    List<Memo> findActiveGeneralMemos();

    @Query("select m from Memo m where m.isActive = true and m.region.id = :regionId order by m.updatedAt desc")
    List<Memo> findActiveRegionalMemos(@Param("regionId") Long regionId);

    @Query("select m from Memo m where m.isActive = true and m.updatedAt > :since and m.region is null order by m.updatedAt desc")
    List<Memo> findUpdatedGeneralMemos(@Param("since") LocalDateTime since);

    @Query("select m from Memo m where m.isActive = true and m.updatedAt > :since and m.region.id = :regionId order by m.updatedAt desc")
    List<Memo> findUpdatedRegionalMemos(@Param("since") LocalDateTime since, @Param("regionId") Long regionId);
}
