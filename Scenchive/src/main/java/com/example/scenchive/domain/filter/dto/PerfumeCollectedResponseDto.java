package com.example.scenchive.domain.filter.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PerfumeCollectedResponseDto {

    private Long id;
    private Long memberId;
    private Long perfumeId;
    private String perfumeName;
    private BrandDto brandDto;

}
