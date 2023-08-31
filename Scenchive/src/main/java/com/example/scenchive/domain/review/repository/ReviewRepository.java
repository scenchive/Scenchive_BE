package com.example.scenchive.domain.review.repository;

import com.example.scenchive.domain.filter.repository.Perfume;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    // 향수 id에 등록된 리뷰 리스트 가져오기
    List<Review> findByPerfumeIdOrderByCreatedAtDesc(Long perfumeId);

    // 전체 별점 합산
    @Query("SELECT SUM(r.rating) FROM Review r WHERE r.perfumeId = :perfumeId")
    double getRatingSumByPerfumeId(Long perfumeId);

    // 해당 향슈의 리뷰 개수 카운트
    long countByPerfumeId(Long perfumeId);

    // 전체 지속력 점수 합산
    @Query("SELECT SUM(r.longevity) FROM Review r WHERE r.perfumeId = :perfumeId")
    double getLongevitySumByPerfumeId(Long perfumeId);

    // 전체 확산력 점수 합산
    @Query("SELECT SUM(r.sillage) FROM Review r WHERE r.perfumeId = :perfumeId")
    double getSillageSumByPerfumeId(Long perfumeId);

    // 계절별 투표수 카운트
    @Query("SELECT r.season, COUNT(r) FROM Review r WHERE r.perfumeId = :perfumeId GROUP BY r.season")
    List<Object[]> getSeasonCountsByPerfumeId(Long perfumeId);
}
