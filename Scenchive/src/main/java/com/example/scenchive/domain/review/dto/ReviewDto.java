package com.example.scenchive.domain.review.dto;

import com.example.scenchive.domain.member.repository.Member;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class ReviewDto {
    private Long perfumeId;
    private int rating; // 별점
    private int longevity; // 지속력
    private int sillage; // 확산력
    private String season; // 계절감
    private String content; // 텍스트 리뷰
    private List<Long> ptagIds; // 향수 키워드 ID 리스트
}
