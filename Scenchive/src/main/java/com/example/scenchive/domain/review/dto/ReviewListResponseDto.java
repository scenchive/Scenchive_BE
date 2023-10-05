package com.example.scenchive.domain.review.dto;

import com.example.scenchive.domain.review.repository.Review;
import lombok.Getter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Getter
public class ReviewListResponseDto {
    private String name; // 작성자 이름
    private String content; // 본문
    private String created_at; // 작성일시
    private String imageUrl;

    public ReviewListResponseDto(Review entity) {
        this.name = entity.getMemberId().getName();
        this.content = entity.getContent();
        this.created_at = entity.getCreated_at().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        this.imageUrl=entity.getMemberId().getImageUrl();
    }
}
