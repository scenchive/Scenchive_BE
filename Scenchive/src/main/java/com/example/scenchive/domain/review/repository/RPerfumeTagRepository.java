package com.example.scenchive.domain.review.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RPerfumeTagRepository extends JpaRepository<RPerfumeTag, Long> {
    // ptag_id가 포함되는 PerfumeTag 리스트(즉, 향수) 조회
    List<RPerfumeTag> findByPtagIn(List<RPTag> ptags);
}
