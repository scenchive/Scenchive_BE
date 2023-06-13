package com.example.scenchive.domain.filter.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PerfumeTagRepository extends JpaRepository<PerfumeTag, Long> {
    // ptag_id가 포함되는 PerfumeTag 리스트(즉, 향수) 조회

    //List<PerfumeTag> findByPtagIdIn(List<Long> ptagIds);

    List<PerfumeTag> findByPtag(PTag ptag);

    //    List<PerfumeTag> findByPtagIdIn(List<Long> ptagIds);
    List<PerfumeTag> findByPtagIn(List<PTag> ptags);

    List<PerfumeTag> findByPerfume(Perfume perfume);
}
