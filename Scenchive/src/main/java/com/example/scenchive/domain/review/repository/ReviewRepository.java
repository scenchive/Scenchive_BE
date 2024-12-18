package com.example.scenchive.domain.review.repository;

import com.example.scenchive.domain.filter.repository.Perfume;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    // 향수 id에 등록된 리뷰 리스트 가져오기
    List<Review> findByPerfumeIdOrderByCreatedAtDesc(Long perfumeId);

    // 전체 별점 합산
    @Query("SELECT SUM(r.rating) FROM Review r WHERE r.perfumeId = :perfumeId")
    double getRatingSumByPerfumeId(@Param("perfumeId") Long perfumeId);

    // 해당 향슈의 리뷰 개수 카운트
    long countByPerfumeId(Long perfumeId);

    // 전체 지속력 점수 합산
    @Query("SELECT SUM(r.longevity) FROM Review r WHERE r.perfumeId = :perfumeId")
    double getLongevitySumByPerfumeId(@Param("perfumeId") Long perfumeId);

    // 전체 확산력 점수 합산
    @Query("SELECT SUM(r.sillage) FROM Review r WHERE r.perfumeId = :perfumeId")
    double getSillageSumByPerfumeId(@Param("perfumeId") Long perfumeId);

    // 계절별 투표수 카운트
    @Query("SELECT r.season, COUNT(r) FROM Review r WHERE r.perfumeId = :perfumeId GROUP BY r.season")
    List<Object[]> getSeasonCountsByPerfumeId(@Param("perfumeId") Long perfumeId);

    // top 5 향수별 리뷰
    @Query("SELECT r.perfumeId, COUNT(r.id) AS reviewCount, p.perfumeName, b.brandName "+
            "FROM Review r JOIN Perfume p ON r.perfumeId = p.id JOIN Brand b ON p.brandId = b.id "+
            "GROUP BY r.perfumeId, p.perfumeName, b.brandName ORDER BY reviewCount DESC, p.perfumeName ASC")
    List<Object[]> getTop5PerfumesReviewCount(Pageable pageable);
}
