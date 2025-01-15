package com.example.scenchive.domain.info.repository;

import com.example.scenchive.domain.filter.repository.Perfume;
import com.example.scenchive.domain.info.dto.ScentAndScentKr;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PerfumescentRepository extends JpaRepository<Perfumescent, Long> {
    // 노트 정보 가져오기
    List<ScentAndScentKr> findByScentContainingOrScentKrContaining(String note1, String note2);

    // 전달 받은 향수 id와 노트 id에 해당하는 Perfumescent의 데이터 row 접근
    List<Perfumescent> findByPerfumeAndPerfumenote(Perfume perfume, Perfumenote perfumenote);

    // 노트 id와 향으로 검색 - 노트 1개 검색
    List<Perfumescent> findByPerfumenoteAndScentKrContaining(Perfumenote perfumenote, String scentKr);

    // 노트 id와 향으로 검색 - 노트 2개 검색
    // JPQL로 쿼리 정의
    @Query("SELECT p1 " +
            "FROM Perfumescent p1, Perfumescent p2 " +
            "WHERE p1.perfumenote = :noteId1 AND p1.scentKr = :scentKr1 " +
            "AND p2.perfumenote = :noteId2 AND p2.scentKr = :scentKr2 " +
            "AND p1.perfume = p2.perfume ")
    List<Perfumescent> find2ByScentKrContaining(
            @Param("noteId1") Perfumenote noteId1,
            @Param("scentKr1") String scentKr1,
            @Param("noteId2") Perfumenote noteId2,
            @Param("scentKr2") String scentKr2
    );

     // 노트 id와 향으로 검색 (탑, 미들, 베이스)
     // JPQL로 쿼리 정의
    @Query("SELECT p1 " +
            "FROM Perfumescent p1, Perfumescent p2, Perfumescent p3 " +
            "WHERE p1.perfumenote = :noteId1 AND p1.scentKr = :scentKr1 " +
            "AND p2.perfumenote = :noteId2 AND p2.scentKr = :scentKr2 " +
            "AND p3.perfumenote = :noteId3 AND p3.scentKr = :scentKr3 " +
            "AND p1.perfume = p2.perfume " +
            "AND p2.perfume = p3.perfume")
    List<Perfumescent> find3ByScentKrContaining(
            @Param("noteId1") Perfumenote noteId1,
            @Param("scentKr1") String scentKr1,
            @Param("noteId2") Perfumenote noteId2,
            @Param("scentKr2") String scentKr2,
            @Param("noteId3") Perfumenote noteId3,
            @Param("scentKr3") String scentKr3
    );
}
