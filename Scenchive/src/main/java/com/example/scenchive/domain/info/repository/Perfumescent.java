package com.example.scenchive.domain.info.repository;

import com.example.scenchive.domain.filter.repository.Perfume;
import com.example.scenchive.domain.member.repository.perfumeMarkedId;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@IdClass(PerfumescentId.class)
@Entity
@Table(name = "perfumescent")
public class Perfumescent {

    @Id
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name = "perfume_id")
    private Perfume perfume;

    @Id
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name = "note_id")
    private Perfumenote perfumenote;

    @Id
    @Column(name = "scent")
    private String scent;

    @Column(name = "scent_kr")
    private String scentKr;

    public Perfumescent(Perfume perfume, Perfumenote perfumenote, String scent, String scentKr) {
        this.perfume = perfume;
        this.perfumenote = perfumenote;
        this.scent = scent;
        this.scentKr = scentKr;
    }
}
