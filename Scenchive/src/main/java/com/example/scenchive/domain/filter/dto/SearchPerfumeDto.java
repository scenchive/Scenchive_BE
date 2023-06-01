package com.example.scenchive.domain.filter.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SearchPerfumeDto {
    private String perfumeName; // 향수 이름 -> 추후 번역 완료되면 한국어 이름 반환으로 변경해야함
    private String brandName; // 브랜드 이름(영어)
    //private String brandName_kr;


    public SearchPerfumeDto(String perfumeName, String brandName) {
        this.perfumeName = perfumeName;
        this.brandName = brandName;
    }
}


