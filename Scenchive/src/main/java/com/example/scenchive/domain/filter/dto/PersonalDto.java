package com.example.scenchive.domain.filter.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class PersonalDto {
    private Long id; // 향수 id
    private String perfumeName; // 향수 이름
    private String brandName; // 브랜드 이름
    private List<Long> keywordIds; // 키워드 id 리스트

    public PersonalDto(Long id, String perfumeName, String brandName, List<Long> keywordIds) {
        this.id = id;
        this.perfumeName = perfumeName;
        this.brandName = brandName;
        this.keywordIds = keywordIds;
    }
}
