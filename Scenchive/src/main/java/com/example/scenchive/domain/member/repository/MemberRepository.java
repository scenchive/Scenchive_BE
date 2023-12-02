package com.example.scenchive.domain.member.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
//

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByName(String name);

    Optional<Member> findByEmail(String email);

    @EntityGraph(attributePaths = "authorities") // 해당 쿼리가 수행될 때, Lazy가 아니고 Eager 조회로 authorites 정보를 가져옴
    Optional<Member> findOneWithAuthoritiesByEmail(String email); // username을 기준으로 User 정보를 가져오는데, 권한 정보도 같이 가져오는 메서드.

    Boolean existsByEmail(String email);

    Boolean existsByName(String name);
}