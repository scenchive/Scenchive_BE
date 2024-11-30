package com.example.scenchive.domain.filter.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PerfumeCollectedRepository extends JpaRepository<PerfumeCollected, Long> {

    // 특정 사용자가 보유한 향수 리스트
    List<PerfumeCollected> findByMemberId(Long memberId);

    // 특정 향수를 보유한 사용자 리스트
    List<PerfumeCollected> findByPerfumeId(Long perfumeId);

    Optional<PerfumeCollected> findByMemberIdAndPerfumeId(Long memberId, Long perfumeId);
}
