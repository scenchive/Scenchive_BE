package com.example.scenchive.domain.comment.repository;
import com.example.scenchive.domain.board.repository.Board;
import com.example.scenchive.domain.BaseTimeEntity;
import com.example.scenchive.domain.member.repository.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static jakarta.persistence.FetchType.LAZY;


@Table(name = "comment")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity

public class Comment extends BaseTimeEntity {

    @Id @GeneratedValue
    @JoinColumn(name = "id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id")
    private Member member;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "board_id")
    private Board board;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "parent_id")
    private Comment parent;

    @Lob
    @Column(nullable = false)
    private String content;
    private boolean isRemoved = false;

    // 부모 댓글을 삭제해도 자식 댓글은 남아 있음
    @OneToMany(mappedBy = "parent") // 자식 댓글을 리스트 형태로 관리
    private List<Comment> childList = new ArrayList<>();

    // 연관관계 편의 메서드
    public void confirmMember(Member member) {
        this.member = member;
        member.addComment(this);
    }

    public void confirmBoard(Board board) {
        this.board = board;
        board.addComment(this);
    }

    public void confirmComment(Comment parent) {
        this.parent = parent;
        parent.addChild(this);
    }

    public void addChild(Comment child) {
        childList.add(child);
    }

    // 삭제
    public void remove() {
        this.isRemoved = true;
    }

    @Builder
    public Comment(Member member, Board board, Comment parent, String content) {
        this.member = member;
        this.board = board;
        this.parent = parent;
        this.content = content;
        this.isRemoved = false;
    }

    // 비즈니스 로직
    public List<Comment> findRemovableList() {
        List<Comment> result = new ArrayList<>();

        Optional.ofNullable(this.parent).ifPresentOrElse(
                parentComment -> { // 1) 대댓글인 경우 (부모 존재하는 경우)
                    if (parentComment.isRemoved() && parentComment.isAllChildRemoved()) {
                        result.addAll(parentComment.getChildList());
                        result.add(parentComment);
                    }
                },

                () -> { // 2) 댓글인 경우
                    if (isAllChildRemoved()) {
                        result.add(this);
                        result.addAll(this.getChildList());
                    }
                }
        );

        return result;
    }

    // 모든 자식 댓글이 삭제되었는지 판단
    private boolean isAllChildRemoved() {
        return getChildList().stream()
                .map(Comment::isRemoved)
                .filter(isRemoved -> !isRemoved) // 지워짐: true, 지워지지 않음: false
                .findAny()// 지워지지 않은 것이 하나라도 있다면 false 반환
                .orElse(true); // 모두 지워진 경우 true 반환
    }

}
