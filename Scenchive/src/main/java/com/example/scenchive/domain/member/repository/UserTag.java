package com.example.scenchive.domain.member.repository;

import com.example.scenchive.domain.board.repository.boardType;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
@Table(name="usertag") //각 사용자가 가지는 태그 모음
public class UserTag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id")
    private Member member;
    //다 쪽에서 FK를 가지고 일 쪽에서 mappedby, list객체 가짐

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="utag_id")
    private Utag utag;
    //다 쪽에서 FK를 가지고 일 쪽에서 mappedby, list객체 가짐

    @Builder
    public UserTag(Member member, Utag utag) {
        this.member = member;
        this.utag = utag;
    }
}
