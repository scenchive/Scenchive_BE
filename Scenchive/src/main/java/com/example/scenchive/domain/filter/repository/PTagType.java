package com.example.scenchive.domain.filter.repository;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@Entity
@Table(name="ptagtype")
public class PTagType { //계열, 분위기, 장소, 계절
    @Id
    private int id;

    private String ptagtype_name;

    private String ptagtype_kr;

    @OneToMany(mappedBy = "ptagType")
    private List<PTag> pTags=new ArrayList<>();
}
