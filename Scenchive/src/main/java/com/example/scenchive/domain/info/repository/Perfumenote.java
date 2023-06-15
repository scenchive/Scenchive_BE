package com.example.scenchive.domain.info.repository;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "perfumenote")
public class Perfumenote {
    @Id
    private Long id;

    @Column(name = "note_name")
    private String noteName;

    @OneToMany(mappedBy = "perfumenote")
    private List<Perfumescent> perfumescentList=new ArrayList<>();

}
