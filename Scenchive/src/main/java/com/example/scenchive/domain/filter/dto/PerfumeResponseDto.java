package com.example.scenchive.domain.filter.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class PerfumeResponseDto {
    private List<PerfumeDto> perfumes;
    private int totalPerfumeCount;

    public PerfumeResponseDto(List<PerfumeDto> perfumes, int totalPerfumeCount) {
        this.perfumes = perfumes;
        this.totalPerfumeCount = totalPerfumeCount;
    }
}
