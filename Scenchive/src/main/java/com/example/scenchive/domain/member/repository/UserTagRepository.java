package com.example.scenchive.domain.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserTagRepository extends JpaRepository<UserTag, Long> {
    boolean existsByMemberAndUtag(Member member, Utag utag);

    //user_id로 utag_id 반환받기
    List<UserTag> findByMember(Member member);

    Optional<UserTag> findByMemberAndUtag(Member member, Utag utag);

    void deleteByMemberAndUtag(Member member, Utag utag);
}
