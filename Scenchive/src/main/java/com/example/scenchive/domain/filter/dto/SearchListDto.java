package com.example.scenchive.domain.filter.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class SearchListDto {
    private List<BrandDto> brands;

    private List<SearchPerfumeDto> perfumes;

    public SearchListDto(List<BrandDto> brands, List<SearchPerfumeDto> perfumes) {
        this.brands = brands;
        this.perfumes = perfumes;
    }
}
