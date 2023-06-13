package com.example.scenchive.domain.filter.repository;

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
@Table(name = "ptag")
public class PTag {
    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name = "id")
    private Long id;

    @JoinColumn(name = "ptag_name")
    private String ptagName;

    @JoinColumn(name = "ptag_kr")
    private String ptagKr;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="ptagtype_id")
    private PTagType ptagType;

    @OneToMany(mappedBy = "ptag")
    private List<PerfumeTag> perfumeTagList=new ArrayList<>();

}
