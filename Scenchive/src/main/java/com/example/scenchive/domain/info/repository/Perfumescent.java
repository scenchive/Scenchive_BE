package com.example.scenchive.domain.info.repository;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "perfumescent")
public class Perfumescent {
    @Id
    @Column(name = "perfume_id")
    private Long perfumeId;

    @Column(name = "note_id")
    private Long noteId;

    @Column(name = "scent")
    private String scent;

    @Column(name = "scent_kr")
    private String scentKr;

    public Perfumescent(Long perfumeId, Long noteId, String scent, String scentKr) {
        this.perfumeId = perfumeId;
        this.noteId = noteId;
        this.scent = scent;
        this.scentKr = scentKr;
    }
}
