package com.example.scenchive.member.repository;

import com.example.scenchive.member.repository.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
//
public interface MemberRepository extends JpaRepository<Member, Long> {
    List<Member> findByName(String name);

    Optional<Member> findByEmail(String email);
}