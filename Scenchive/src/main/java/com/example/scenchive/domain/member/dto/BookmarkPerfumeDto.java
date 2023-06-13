package com.example.scenchive.domain.member.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class BookmarkPerfumeDto {

    private String perfume_name;

    private String brand_name;

    public BookmarkPerfumeDto(String perfume_name, String brand_name) {
        this.perfume_name = perfume_name;
        this.brand_name = brand_name;
    }
}
