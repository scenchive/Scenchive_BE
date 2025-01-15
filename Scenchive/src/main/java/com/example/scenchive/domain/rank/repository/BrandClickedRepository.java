package com.example.scenchive.domain.rank.repository;

import com.example.scenchive.domain.filter.repository.Brand;
import com.example.scenchive.domain.filter.repository.Perfume;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BrandClickedRepository extends JpaRepository<BrandClicked, Long> {

    // 브랜드 클릭 수 반환
    BrandClicked findByBrand(Brand brand);

}
