package com.example.scenchive.domain.review.controller;

import com.example.scenchive.domain.review.dto.ReviewDto;
import com.example.scenchive.domain.review.service.ReviewService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.CrossOrigin;


@RestController
@RequestMapping("/review")
@CrossOrigin(origins="http://10.0.2.15:8081")
public class ReviewController {
    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @PostMapping("/")
    public ResponseEntity<String> saveReview(@RequestBody ReviewDto reviewDto) {
        try {
            reviewService.saveReview(reviewDto);
            return ResponseEntity.ok("리뷰가 성공적으로 등록되었습니다.");
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }

    @DeleteMapping("/{reviewId}")
    public ResponseEntity<String> deleteReview(@PathVariable("reviewId") Long reviewId) {
        reviewService.deleteReview(reviewId);
        return ResponseEntity.ok("리뷰가 성공적으로 삭제되었습니다.");
    }
}
