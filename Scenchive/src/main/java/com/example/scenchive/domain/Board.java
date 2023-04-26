package com.example.scenchive.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
public class Board extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="id")
    private Long user_id;

    private String title;

    private String body;

    @ManyToOne
    @JoinColumn(name="id")
    private Long boardtype_id;

    @Builder
    public Board(Long user_id, String title, String body, Long boardtype_id) {
        this.user_id = user_id;
        this.title = title;
        this.body = body;
        this.boardtype_id = boardtype_id;
    }
}
