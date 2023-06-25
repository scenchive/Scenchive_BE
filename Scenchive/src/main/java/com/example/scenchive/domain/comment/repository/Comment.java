package com.example.scenchive.domain.comment.repository;

import com.example.scenchive.domain.BaseTimeEntity;
import com.example.scenchive.domain.board.repository.Board;
import com.example.scenchive.domain.member.repository.Member;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "comment")
public class Comment extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    @JsonBackReference // 순환참조 방어 (자식 클래스에 추가)
    private Board board;

    @Column(nullable = false)
    private String content;

    private boolean deleted; // 댓글 삭제 여부 관리

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Comment parentComment; // 댓글-대댓글 부모 자식 관계 설정

    @Builder
    public Comment(Member member, Board board, String content, Comment parentComment) {
        this.member = member;
        this.board = board;
        this.content = content;
        this.parentComment = parentComment;
        this.deleted = false;
    }

    // 댓글 삭제
    public void delete() {
        this.deleted = true;
    }
}
