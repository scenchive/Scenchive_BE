package com.example.scenchive.domain.filter.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public interface PerfumeCollectedRepository extends JpaRepository<PerfumeCollected, Long> {

    // 특정 사용자가 보유한 향수 리스트
    List<PerfumeCollected> findByMemberId(Long memberId);

    // 특정 향수를 보유한 사용자 리스트
    List<PerfumeCollected> findByPerfumeId(Long perfumeId);

    // 특정 사용자와 특정 향수 확인
    Optional<PerfumeCollected> findByMemberIdAndPerfumeId(Long memberId, Long perfumeId);

    // 가장 많이 보유된 향수
    @Query(value = "SELECT p.perfume_name AS name, COUNT(pc.id) AS count " +
            "FROM perfumecollected pc " +
            "JOIN perfume p ON pc.perfume_id = p.id " +
            "GROUP BY p.perfume_name " +
            "HAVING COUNT(pc.id) = (SELECT MAX(count) FROM (SELECT COUNT(pc.id) AS count " +
            "FROM perfumecollected pc GROUP BY pc.perfume_id) AS subquery)", nativeQuery = true)
    List<Map<String, Object>> findMostCollectedPerfume();

    // 가장 많이 보유된 향수 브랜드
    @Query(value = "SELECT b.brand_name AS name, COUNT(pc.id) AS count " +
            "FROM perfumecollected pc " +
            "JOIN perfume p ON pc.perfume_id = p.id " +
            "JOIN brand b ON p.brand_id = b.id " +
            "GROUP BY b.brand_name " +
            "HAVING COUNT(pc.id) = (" +
            "    SELECT MAX(brand_count) FROM (" +
            "        SELECT COUNT(pc_inner.id) AS brand_count " +
            "        FROM perfumecollected pc_inner " +
            "        JOIN perfume p_inner ON pc_inner.perfume_id = p_inner.id " +
            "        JOIN brand b_inner ON p_inner.brand_id = b_inner.id " +
            "        GROUP BY b_inner.brand_name" +
            "    ) AS counts" +
            ") " +
            "ORDER BY b.brand_name", nativeQuery = true)
    List<Map<String, Object>> findMostCollectedBrand();

    // 유저 평균 향수 개수
    @Query(value = "SELECT AVG(perfume_count) AS avg_count "+
                    "FROM ( "+
                            " SELECT COUNT(pc.id) AS perfume_count "+
                            " FROM perfumecollected pc "+
                            " GROUP BY pc.member_id "+
                            ") AS user_perfume_counts", nativeQuery = true)
    Double findAveragePerfumeCount();

    // 보유 향수 가장 많은 유저 닉네임, 향수 개수
    @Query(value = "SELECT m.name AS name, COUNT(pc.id) AS count " +
            "FROM user m " +
            "JOIN perfumecollected pc ON m.id = pc.user_id " +
            "GROUP BY m.id, m.name " +
            "HAVING COUNT(pc.id) = (" +
            "    SELECT MAX(user_count) FROM (" +
            "        SELECT COUNT(inner_pc.id) AS user_count " +
            "        FROM perfumecollected inner_pc " +
            "        GROUP BY inner_pc.user_id" +
            "    ) AS subquery" +
            ") " +
            "ORDER BY m.name", nativeQuery = true)
    List<Map<String, Object>> findUserWithMostCollectedPerfumes();

}