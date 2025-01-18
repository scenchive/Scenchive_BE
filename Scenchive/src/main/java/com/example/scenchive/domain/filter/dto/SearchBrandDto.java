package com.example.scenchive.domain.filter.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SearchBrandDto {
    private Long brandId;
    private String brandName; // 브랜드 이름(영어)

    private String brandName_kr; // 브랜드 이름(한국어)
    private String brandImage;


   public SearchBrandDto(Long brandId, String brandName, String brandName_kr, String brandImage) {
        this.brandId = brandId;
        this.brandName = brandName;
        this.brandName_kr=brandName_kr;
        this.brandImage=brandImage;
    }
}