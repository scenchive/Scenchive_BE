package com.example.scenchive.domain.rank.repository;

import com.example.scenchive.domain.filter.repository.Perfume;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PerfumeClickedRepository extends JpaRepository<PerfumeClicked, Long> {
    // 향수 클릭 수 반환
    PerfumeClicked findByPerfume(Perfume perfume);

    // 클릭 수 많은 순대로 5개 출력
    List<PerfumeClickedMapping> findBySeasonOrderByClickCountDesc(Season season, Pageable pageable);

    // 클릭 수 증가 쿼리 (Custom Query)
//    @Modifying
//    @Query("UPDATE PerfumeClicked ic SET ic.clickCount = ic.clickCount + 1 WHERE ic.perfumeId = :perfumeId)
//    void incrementClickCount(Long perfumeId);

}
