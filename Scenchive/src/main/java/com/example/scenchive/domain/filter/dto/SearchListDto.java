package com.example.scenchive.domain.filter.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class SearchListDto {
    int brandsNum;
    private List<BrandDto> brands;
    int perfumesNum;
    private List<SearchPerfumeDto> perfumes;
    private String errorMessage;

    public SearchListDto(int brandsNum, List<BrandDto> brands, int perfumesNum, List<SearchPerfumeDto> perfumes) {
        this.brandsNum = brandsNum;
        this.brands = brands;
        this.perfumesNum = perfumesNum;
        this.perfumes = perfumes;
        this.errorMessage = null;
    }
    public SearchListDto(String errorMessage) {
        this.errorMessage = errorMessage;
        this.brandsNum = 0;
        this.brands = null;
        this.perfumesNum = 0;
        this.perfumes = null;
    }
}
