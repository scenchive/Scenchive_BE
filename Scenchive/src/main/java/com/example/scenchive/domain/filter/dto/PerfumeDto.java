package com.example.scenchive.domain.filter.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class PerfumeDto {
    private Long id; // 향수 id
    private String perfumeName; // 향수 이름
    private String perfumeImage; // 향수 이미지
    private String brandName; // 브랜드 이름
    private String brandName_kr; // 브랜드 이름 (한글)
    private List<Long> keywordIds; // 키워드 id 리스트

    public PerfumeDto(Long id, String perfumeName, String perfumeImage, String brandName, String brandName_kr, List<Long> keywordIds) {
        this.id = id;
        this.perfumeName = perfumeName;
        this.perfumeImage = perfumeImage;
        this.brandName = brandName;
        this.brandName_kr = brandName_kr;
        this.keywordIds = keywordIds;
    }
}
