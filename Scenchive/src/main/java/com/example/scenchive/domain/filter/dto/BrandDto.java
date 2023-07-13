package com.example.scenchive.domain.filter.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class BrandDto {
    private String brandName;

    private String brandName_kr;

    public BrandDto(String brandName, String brandName_kr) {
        this.brandName = brandName;
        this.brandName_kr = brandName_kr;
    }

    //이미지 추가 필요
}
