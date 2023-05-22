package com.example.scenchive.domain.filter.dto;

import com.example.scenchive.domain.filter.repository.Perfume;
import lombok.Getter;

@Getter
public class PerfumeDto {
    private Long id; // 향수 id
    private String perfumeName; // 향수 이름

    public PerfumeDto(Long id, String perfumeName) {
        this.id = id;
        this.perfumeName = perfumeName;
    }
}
