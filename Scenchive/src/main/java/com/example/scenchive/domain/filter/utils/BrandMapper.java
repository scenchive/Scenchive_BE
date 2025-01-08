package com.example.scenchive.domain.filter.utils;

import com.example.scenchive.domain.filter.dto.BrandDto;
import com.example.scenchive.domain.filter.repository.Brand;

public class BrandMapper {
    // Entity -> Dto
    public static BrandDto toBrandDto(Brand brand){
        return new BrandDto(
                brand.getId(),
                brand.getBrandName(),
                brand.getBrandName_kr(),
                brand.getBrandUrl()
        );
    }

    // Dto -> Entity
//    public static Brand toBrandEntity(BrandDto brandDto){
//        return new Brand(
//                brandDto.getBrandId(),
//                brandDto.getBrandName(),
//                brandDto.getBrandName_kr(),
//                brandDto.getBrandImage()
//        );
//    }
}
