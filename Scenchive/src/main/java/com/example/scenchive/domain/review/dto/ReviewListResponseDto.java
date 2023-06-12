package com.example.scenchive.domain.review.dto;

import com.example.scenchive.domain.review.repository.Review;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ReviewListResponseDto {
    private String name; // 작성자 이름
    private String content; // 본문
    private LocalDateTime created_at; // 작성일시

    public ReviewListResponseDto(Review entity) {
        this.name = entity.getMemberId().getName();
        this.content = entity.getContent();
        this.created_at = entity.getCreated_at();
    }
}
