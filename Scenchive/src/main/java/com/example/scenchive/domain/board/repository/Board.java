package com.example.scenchive.domain.board.repository;

import com.example.scenchive.domain.BaseTimeEntity;
import com.example.scenchive.domain.comment.repository.Comment;
import com.example.scenchive.domain.member.repository.Member;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Getter
@ToString
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

    @Column(length=1000)
    private String body;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="boardtype_id")
    private boardType boardtype;
    //다 쪽에서 FK를 가지고 일 쪽에서 mappedby, list객체 가짐

    @Column
    private String imageUrl;

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

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

    public List<Comment> getComments() {
        return comments;
    }

    @Builder
    public Board(Member member, String title, String body, boardType boardtype, String imageUrl) {
        this.member = member;
        this.title = title;
        this.body = body;
        this.boardtype = boardtype;
        this.imageUrl=imageUrl;
    }

    public void update(String title, String body, boardType boardtype, String imageUrl){
        this.title=title;
        this.body = body;
        this.boardtype = boardtype;
        this.imageUrl=imageUrl;
    }

    public void deleteImage(String imageUrl){
        this.imageUrl=null;
    }
}
