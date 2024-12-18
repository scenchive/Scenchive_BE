package com.example.scenchive.domain.review.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PerfumeReviewCountDto {
    private Long perfumeId;
    private String perfumeName;
    private String brandName;
    private long reviewCount;
}
