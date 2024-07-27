package com.example.scenchive.domain.filter.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PerfumeRepository extends JpaRepository<Perfume, Long> {
    List<Perfume> findByPerfumeNameContainingIgnoreCase(String perfumeName);

    List<Perfume> findByBrandId(Long brandId);

    void deleteById(Long Id);

    @Query(value = "insert into perfume (brand_id, perfume_name, perfume_kr) values (:brandId, :perfumeName, :perfumekr)", nativeQuery = true)
    Perfume save(@Param("brandId") Long brandId, @Param("perfumeName") String perfumeName, @Param("perfumekr") String perfumekr);
}
