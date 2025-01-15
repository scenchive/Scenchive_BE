package com.example.scenchive.domain.review.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PerfumeDetailsDto {
    private Long perfumeId;
    private String perfumeName;
    private String perfumeKr;
    private String perfumeImage;
    private String brandName;
    private String brandNameKr;
}
