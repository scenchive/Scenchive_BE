package com.example.scenchive.domain.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserTagRepository extends JpaRepository<UserTag, Long> {
    boolean existsByMemberAndUtag(Member member, Utag utag);

    //user_id로 utag_id 반환받기
    List<UserTag> findByMember(Member member);
}
