package com.example.scenchive.domain.member.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class perfumeMarkedDto {

    private Long userId;

    private Long perfumeId;

    public perfumeMarkedDto(Long userId, Long perfumeId) {
        this.userId = userId;
        this.perfumeId = perfumeId;
    }
}
