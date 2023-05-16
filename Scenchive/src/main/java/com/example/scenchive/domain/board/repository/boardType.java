package com.example.scenchive.domain.board.repository;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
//
@Getter
@NoArgsConstructor
@Entity
@Table(name="board_type")
public class boardType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String boardtype_name;

    @OneToMany(mappedBy = "boardtype")
    private List<Board> boards=new ArrayList<>();
    //다 쪽에서 FK를 가지고 일 쪽에서 mappedby, list객체 가짐

    public boardType(int id) {this.id = id;}
    public boardType(String boardtype_name) {
        this.boardtype_name = boardtype_name;
    }
}
