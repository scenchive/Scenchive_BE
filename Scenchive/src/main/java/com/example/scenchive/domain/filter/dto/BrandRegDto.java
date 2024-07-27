package com.example.scenchive.domain.filter.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class BrandRegDto {
    private String brandName;
    private String brandName_kr;

    public BrandRegDto(String brandName, String brandName_kr) {
        this.brandName = brandName;
        this.brandName_kr = brandName_kr;
    }
}
