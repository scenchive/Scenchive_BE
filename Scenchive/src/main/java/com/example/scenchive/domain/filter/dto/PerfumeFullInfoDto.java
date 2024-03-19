package com.example.scenchive.domain.filter.dto;

import lombok.Getter;

@Getter
public class PerfumeFullInfoDto {
    private Long id; // 향수 id
    private String perfumeName; // 향수 이름
    private String perfume_kr; //향수 한국어 이름
    private String perfumeImage; // 향수 이미지
    private String brandName; // 브랜드 이름
    private String brandName_kr; // 브랜드 이름 (한글)
    private String brandImage;

    public PerfumeFullInfoDto(Long id, String perfumeName, String perfume_kr, String perfumeImage, String brandName, String brandName_kr, String brandImage) {
        this.id = id;
        this.perfumeName = perfumeName;
        this.perfume_kr=perfume_kr;
        this.perfumeImage = perfumeImage;
        this.brandName = brandName;
        this.brandName_kr = brandName_kr;
        this.brandImage=brandImage;
    }
}
