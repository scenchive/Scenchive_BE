package com.example.scenchive.domain.member.repository;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@Entity
@Table(name="utag") //세부 코드
public class Utag {
    @Id
    private Long id;

    private String utag;

    private String utag_kr;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="utagtype_id")
    private UtagType utagtype;
    //다 쪽에서 FK를 가지고 일 쪽에서 mappedby, list객체 가짐

    @OneToMany(mappedBy = "utag")
    private List<UserTag> usertags=new ArrayList<>();
    //다 쪽에서 FK를 가지고 일 쪽에서 mappedby, list객체 가짐

    @Builder
    public Utag(Long id, String utag, String utag_kr, UtagType utagtype) {
        this.id = id;
        this.utag = utag;
        this.utag_kr = utag_kr;
        this.utagtype = utagtype;
    }
}
