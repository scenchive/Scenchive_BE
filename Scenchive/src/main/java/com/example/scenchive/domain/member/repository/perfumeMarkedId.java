package com.example.scenchive.domain.member.repository;

import com.example.scenchive.domain.filter.repository.Perfume;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
public class perfumeMarkedId implements Serializable {
    private Member member;
    private Perfume perfume;
}
