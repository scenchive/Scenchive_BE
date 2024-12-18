package com.example.scenchive.domain.review.controller;

import com.example.scenchive.domain.review.dto.PerfumeRatingDto;
import com.example.scenchive.domain.review.dto.PerfumeReviewCountDto;
import com.example.scenchive.domain.review.dto.ReviewDto;
import com.example.scenchive.domain.review.dto.ReviewListResponseDto;
import com.example.scenchive.domain.review.repository.RPerfumeTagRepository;
import com.example.scenchive.domain.review.service.ReviewService;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.List;


@RestController
@CrossOrigin(origins = {"http://localhost:3000", "https://scenchive.github.io/"}, allowCredentials = "true", allowedHeaders = "Authorization")
public class ReviewController {
    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    // 리뷰 등록
    @PostMapping("/review/")
    public ResponseEntity<String> saveReview(@RequestBody ReviewDto reviewDto) {
        try {
            reviewService.saveReview(reviewDto);
            return ResponseEntity.ok("리뷰가 성공적으로 등록되었습니다.");
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }

    // 리뷰 삭제
    @DeleteMapping("/review/{reviewId}")
    public ResponseEntity<String> deleteReview(@PathVariable("reviewId") Long reviewId) {
        reviewService.deleteReview(reviewId);
        return ResponseEntity.ok("리뷰가 성공적으로 삭제되었습니다.");
    }

    // 향수별 리뷰 조회
    @GetMapping("/reviews/{perfumeId}")
    public List<ReviewListResponseDto> getReview(@PathVariable("perfumeId") Long perfumeId, Model model) {
        model.addAttribute("perfumereview", reviewService.findByPerfumeId(perfumeId));
        return reviewService.findByPerfumeId(perfumeId);
    }

    // 향수별 평점평균 조회
    @GetMapping("/perfumerating/{perfumeId}")
    public ResponseEntity<PerfumeRatingDto> getPerfumeRating(@PathVariable("perfumeId") Long perfumeId) {
        PerfumeRatingDto ratingDto = reviewService.calculatePerfumeRating(perfumeId);
        return ResponseEntity.ok(ratingDto);
    }

    // 리뷰 많은 향수 top5
    @GetMapping("/main/top-reviewed-perfumes")
    public ResponseEntity<List<PerfumeReviewCountDto>> getTop5ReviewedPerfumes(){
        List<PerfumeReviewCountDto> topPerfumes = reviewService.getTop5PerfumesByReviews();
        return ResponseEntity.ok(topPerfumes);
    }
}
