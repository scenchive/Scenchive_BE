package com.example.scenchive.domain.member.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class BookmarkPerfumeDto { //향수 아이디, 이미지, 이름, 브랜드

    private Long perfume_id;
    private String perfume_name;
    private String perfumeImage;
    private String brand_name;

    public BookmarkPerfumeDto(Long perfume_id, String perfume_name, String perfumeImage, String brand_name) {
        this.perfume_id=perfume_id;
        this.perfume_name = perfume_name;
        this.perfumeImage = perfumeImage;
        this.brand_name = brand_name;
    }
}
