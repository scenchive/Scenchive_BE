package com.example.scenchive.member.repository;

import com.example.scenchive.board.repository.Board;
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

    private int password;

    @OneToMany(mappedBy = "member")
    private List<Board> boards=new ArrayList<>();
    //다 쪽에서 FK를 가지고 일 쪽에서 mappedby, list객체 가짐

    @Builder
    public Member(String email, String name, int password) {
        this.email = email;
        this.name = name;
        this.password = password;
    }

    public boolean checkPassword(int password){
        return this.password==password;
    }
}
