package com.example.scenchive.domain.filter.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class BrandPerfumeResponseDto {
    private long totalBrandPerfumeCount;
    private List<SearchPerfumeDto> perfumes;

    public BrandPerfumeResponseDto(long totalBrandPerfumeCount, List<SearchPerfumeDto> perfumes) {
        this.totalBrandPerfumeCount = totalBrandPerfumeCount;
        this.perfumes = perfumes;
    }
}
