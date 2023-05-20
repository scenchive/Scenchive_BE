package com.example.scenchive.domain.member.repository;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@Entity
@Table(name="utagtype") //계열, 분위기
public class UtagType {
    @Id
    private int id;

    private String utagtype_name;

    private String utagtype_kr;

    @OneToMany(mappedBy = "utagtype")
    private List<Utag> utags=new ArrayList<>();
    //다 쪽에서 FK를 가지고 일 쪽에서 mappedby, list객체 가짐

}
