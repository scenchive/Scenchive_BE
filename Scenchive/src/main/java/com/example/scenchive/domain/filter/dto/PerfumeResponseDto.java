package com.example.scenchive.domain.filter.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class PerfumeResponseDto {
    private long totalPerfumeCount;
    private List<PerfumeDto> perfumes;


    public PerfumeResponseDto(long totalPerfumeCount, List<PerfumeDto> perfumes) {
        this.totalPerfumeCount = totalPerfumeCount;
        this.perfumes = perfumes;
    }
}
