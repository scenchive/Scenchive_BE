package com.example.scenchive.domain.filter.repository;

import com.example.scenchive.domain.member.repository.Member;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "perfumecollected")
public class PerfumeCollected {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "perfume_id")
    private Perfume perfume;
}
