package com.example.scenchive.domain.filter.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class MainPerfumeDto {
    private Long id; // 향수 id
    private String perfumeName; // 향수 이름
    private String perfumeImage; // 향수 이미지
    private String brandName; // 브랜드 이름
    private List<Long> keywordIds; // 키워드 id 리스트
    private double ratingAvg; //향수 리뷰 평점평균

    public MainPerfumeDto(Long id, String perfumeName, String perfumeImage, String brandName, List<Long> keywordIds, double ratingAvg) {
        this.id = id;
        this.perfumeName = perfumeName;
        this.perfumeImage = perfumeImage;
        this.brandName = brandName;
        this.keywordIds = keywordIds;
        this.ratingAvg = ratingAvg;
    }
}
