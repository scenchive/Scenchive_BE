package com.example.scenchive.domain.rank.repository;

import com.example.scenchive.domain.board.repository.Board;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "season")
public class Season {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Enumerated(EnumType.STRING)
    private SeasonName name;

    @OneToMany(mappedBy = "season")
    private List<PerfumeClicked> perfumeClickedList = new ArrayList<>();

    public Season(SeasonName name) {
        this.name = name;
    }

}
