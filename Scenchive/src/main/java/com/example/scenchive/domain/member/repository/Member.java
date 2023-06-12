package com.example.scenchive.domain.member.repository;

import com.example.scenchive.domain.board.repository.Board;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@Entity
@Table(name="user")
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;

    private String name;

    private String password;


    @OneToMany(mappedBy = "member")
    private List<Board> boards=new ArrayList<>();
    //다 쪽에서 FK를 가지고 일 쪽에서 mappedby, list객체 가짐

    @OneToMany(mappedBy="member")
    private List<UserTag> userTags=new ArrayList<>();

    @OneToMany(mappedBy = "member")
    private List<perfumeMarked> perfumeMarkedList=new ArrayList<>();

//    @OneToMany(mappedBy = "member", cascade = ALL, orphanRemoval = true)
//    private List<Comment> commentList = new ArrayList<>();

//    public void addComment(Comment comment) {
//        commentList.add(comment);
//    }

    @Builder
    public Member(String email, String name, String password) {
        this.email = email;
        this.name = name;
        this.password = password;
    }

    public Member(String name) {
        this.name = name;
    }

    public boolean checkPassword(String password){
        return this.password.equals(password);
    }
}
