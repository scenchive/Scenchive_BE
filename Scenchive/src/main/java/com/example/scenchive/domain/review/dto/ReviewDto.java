package com.example.scenchive.domain.review.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class ReviewDto {
    private Long memberId;
    private Long perfumeId;
    private int rating;
    private int longevity;
    private int sillage;
    private String content;
    private List<Long> ptagIds; // 향수 키워드 ID 리스트
}
