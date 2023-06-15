package com.example.scenchive.domain.info.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PerfumescentRepository extends JpaRepository<Perfumescent, Long> {
    // 전달 받은 향수 id와 노트 id에 해당하는 Perfumescent의 데이터 row 접근
    List<Perfumescent> findByPerfumeIdAndNoteId(Long perfumeId, Long noteId);

    // 향수 id에 해당하는 모든 Perfumescent row 접근
    List<Perfumescent> findByPerfumeId(Long perfumeId);
}
