package com.example.scenchive.domain.filter.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SearchPerfumeDto {
    private Long perfumeId;
    private String perfumeName; // 향수 이름

    private String perfume_kr; //향수 한국어 이름
    private String perfumeImage;
    private Long brandId;
    private String brandName; // 브랜드 이름(영어)

    private String brandName_kr; // 브랜드 이름(한국어)
    private String brandUrl;

    //이미지 추가 필요요

   public SearchPerfumeDto(Long perfumeId, String perfumeName, String perfume_kr, String perfumeImage,
                           Long brandId, String brandName, String brandName_kr, String brandUrl) {
        this.perfumeId=perfumeId;
        this.perfumeName = perfumeName;
        this.perfume_kr=perfume_kr;
        this.perfumeImage = perfumeImage;
        this.brandId = brandId;
        this.brandName = brandName;
        this.brandName_kr=brandName_kr;
        this.brandUrl=brandUrl;
    }
}