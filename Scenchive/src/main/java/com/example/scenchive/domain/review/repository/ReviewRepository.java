package com.example.scenchive.domain.review.repository;

import com.example.scenchive.domain.filter.repository.Perfume;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
//    List<Review> findByPerfumeId(Perfume perfume);
    List<Review> findByPerfumeId(Long perfumeId);
}
