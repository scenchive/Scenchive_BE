package com.example.scenchive.domain.info.repository;

import com.example.scenchive.domain.filter.repository.Perfume;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PerfumescentRepository extends JpaRepository<Perfumescent, Long> {
    // 전달 받은 향수 id와 노트 id에 해당하는 Perfumescent의 데이터 row 접근
    List<Perfumescent> findByPerfumeAndPerfumenote(Perfume perfume, Perfumenote perfumenote);

    // 노트 id와 향으로 검색
    List<Perfumescent> findByPerfumenoteAndScentKrContaining(Perfumenote perfumenote, String scentKr);
}
