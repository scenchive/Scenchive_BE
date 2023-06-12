package com.example.scenchive.domain.member.repository;

import com.example.scenchive.domain.filter.repository.Perfume;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Getter
@NoArgsConstructor
@IdClass(perfumeMarkedId.class)
@Entity
@Table(name="perfumemarked")
public class perfumeMarked {

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private Member member;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "perfume_id")
    private Perfume perfume;

    @Builder
    public perfumeMarked(Member member, Perfume perfume) {
        this.member = member;
        this.perfume = perfume;
    }
}
