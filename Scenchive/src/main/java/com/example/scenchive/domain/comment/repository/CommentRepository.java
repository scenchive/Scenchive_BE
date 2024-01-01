package com.example.scenchive.domain.comment.repository;

import com.example.scenchive.domain.member.repository.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByMember(Member member);
}
