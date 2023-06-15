package com.example.scenchive.domain.info.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PerfumenoteRepository extends JpaRepository<Perfumenote, Long> {
    Optional<Perfumenote> findById(Long noteId);
}
