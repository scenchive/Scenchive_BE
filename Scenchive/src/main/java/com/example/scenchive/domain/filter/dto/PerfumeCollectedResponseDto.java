package com.example.scenchive.domain.filter.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PerfumeCollectedResponseDto {
    private Long perfumeId;
    private String perfumeName;
    private String perfumeKr;
    private String perfumeImage;
    private String brandName;
    private String brandNameKr;
}
