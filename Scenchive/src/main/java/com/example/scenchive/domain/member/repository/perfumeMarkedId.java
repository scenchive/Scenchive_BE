package com.example.scenchive.domain.member.repository;

import com.example.scenchive.domain.filter.repository.Perfume;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public class perfumeMarkedId {
    private Member member;
    private Perfume perfume;
}
