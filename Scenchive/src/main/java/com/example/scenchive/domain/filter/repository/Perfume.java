package com.example.scenchive.domain.filter.repository;

import com.example.scenchive.domain.info.repository.Perfumescent;
import com.example.scenchive.domain.member.repository.perfumeMarked;
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
@Table(name = "perfume")
public class Perfume {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "perfume_name")
    private String perfumeName;

    @Column(columnDefinition = "TEXT")
    private String perfume_kr;

    @JoinColumn(name = "brand_id")
    private Long brandId;

    public Perfume(String perfumeName, String perfume_kr, Long brandId) {
        this.perfumeName = perfumeName;
        this.perfume_kr = perfume_kr;
        this.brandId = brandId;
    }

    public Perfume(Long id) {
        this.id = id;
    }

    @OneToMany(mappedBy = "perfume")
    private List<perfumeMarked> perfumeMarkedList=new ArrayList<>();

    @OneToMany(mappedBy = "perfume")
    private List<Perfumescent> perfumescentList=new ArrayList<>();

    @OneToMany(mappedBy = "perfume")
    private List<PerfumeCollected> collectedByUsers = new ArrayList<>();


    public void update(String perfumeKr){
        this.perfume_kr=perfumeKr;
    }
}
