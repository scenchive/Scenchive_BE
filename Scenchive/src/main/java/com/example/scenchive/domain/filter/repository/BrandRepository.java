package com.example.scenchive.domain.filter.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BrandRepository extends JpaRepository<Brand, Long> {
    List<Brand> findByBrandName(String brandName);

    List<Brand> findByBrandNameContainingIgnoreCase(String brandname);

    @Query("SELECT b FROM Brand b WHERE LOWER(b.brandName_kr) LIKE LOWER(CONCAT('%', :brandname, '%'))")
    List<Brand> findByBrandNameKrContainingIgnoreCase(@Param("brandname") String brandname);

}
