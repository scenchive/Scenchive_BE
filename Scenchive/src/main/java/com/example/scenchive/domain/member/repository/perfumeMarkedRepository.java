package com.example.scenchive.domain.member.repository;

import com.example.scenchive.domain.filter.repository.Perfume;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface perfumeMarkedRepository extends JpaRepository<perfumeMarked, Long> {
    Optional<perfumeMarked> findByMemberAndPerfume(Member member, Perfume perfume);

    List<perfumeMarked> findByMember(Member member);
}
