package com.example.scenchive.domain.filter.repository;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@Entity
@IdClass(PerfumeTag.PerfumeTagId.class)
@Table(name = "perfumetag")
public class PerfumeTag {
    @Id
    @ManyToOne
    @JoinColumn(name = "perfume_id")
    private Perfume perfume;

    @Id
    @ManyToOne
    @JoinColumn(name = "ptag_id")
    private PTag ptag;

    public static class PerfumeTagId implements Serializable {
        private Perfume perfume;
        private PTag ptag;

        public PerfumeTagId() {

        }
    }
}