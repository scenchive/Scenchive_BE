package com.example.scenchive.domain.filter.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SearchPerfumeDto {

    private Long perfumeId;
    private String perfumeName; // 향수 이름 -> 추후 번역 완료되면 한국어 이름 반환으로 변경해야함
    private Long brandId;
    private String brandName; // 브랜드 이름(영어)
    //private String brandName_kr;

    public SearchPerfumeDto(Long perfumeId, String perfumeName, Long brandId, String brandName) {
        this.perfumeId=perfumeId;
        this.perfumeName = perfumeName;
        this.brandId = brandId;
        this.brandName = brandName;
    }
}