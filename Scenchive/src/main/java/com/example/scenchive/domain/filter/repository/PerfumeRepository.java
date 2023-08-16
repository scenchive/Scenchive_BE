package com.example.scenchive.domain.filter.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PerfumeRepository extends JpaRepository<Perfume, Long> {
    List<Perfume> findByPerfumeNameContainingIgnoreCase(String perfumeName);

    List<Perfume> findByBrandId(Long brandId);


}
