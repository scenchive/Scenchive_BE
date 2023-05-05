package com.example.scenchive.board.repository;

import com.example.scenchive.comment.Comment;
import com.example.scenchive.domain.BaseTimeEntity;
import com.example.scenchive.member.repository.Member;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.CascadeType.ALL;

@Getter
@NoArgsConstructor
@Entity
@Table(name="board")
public class Board extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id")
    private Member member;
    //다 쪽에서 FK를 가지고 일 쪽에서 mappedby, list객체 가짐

    private String title;

    private String body;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="boardtype_id")
    private boardType boardtype;
    //다 쪽에서 FK를 가지고 일 쪽에서 mappedby, list객체 가짐

    @OneToMany(mappedBy = "post", cascade = ALL, orphanRemoval = true)
    private List<Comment> commentList = new ArrayList<>();

    //연관관계 메서드
    public void addMember(Member member){
        this.member=member;
        member.getBoards().add(this);
    }

    //연관관계 메서드
    public void addBoardtype(boardType boardtype){
        this.boardtype=boardtype;
        boardtype.getBoards().add(this);
    }

    public void addComment(Comment comment) {
        commentList.add(comment);
    }

    @Builder
    public Board(Member member, String title, String body, boardType boardtype) {
        this.member = member;
        this.title = title;
        this.body = body;
        this.boardtype = boardtype;
    }

    public void update(String title, String body, boardType boardtype){
        this.title=title;
        this.body = body;
        this.boardtype = boardtype;
    }
}
