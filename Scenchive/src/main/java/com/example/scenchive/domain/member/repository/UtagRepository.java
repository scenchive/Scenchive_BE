package com.example.scenchive.domain.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UtagRepository extends JpaRepository<Utag, Long> {
}
