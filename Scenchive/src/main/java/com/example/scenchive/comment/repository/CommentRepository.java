package com.example.scenchive.comment.repository;

import com.example.scenchive.comment.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
