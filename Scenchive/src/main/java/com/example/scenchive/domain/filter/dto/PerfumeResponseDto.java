package com.example.scenchive.domain.filter.dto;

import java.util.List;

public class PerfumeResponseDto {
    private List<PerfumeDto> perfumes;
    private long totalPerfumeCount;

    public PerfumeResponseDto(List<PerfumeDto> perfumes, long totalPerfumeCount) {
        this.perfumes = perfumes;
        this.totalPerfumeCount = totalPerfumeCount;
    }
}
