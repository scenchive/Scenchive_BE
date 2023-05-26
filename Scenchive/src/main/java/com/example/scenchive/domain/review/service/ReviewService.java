package com.example.scenchive.domain.review.service;

import com.example.scenchive.domain.filter.repository.PerfumeTag;
import com.example.scenchive.domain.filter.repository.PerfumeTagRepository;
import com.example.scenchive.domain.review.dto.ReviewDto;
import com.example.scenchive.domain.review.repository.Review;
import com.example.scenchive.domain.review.repository.ReviewRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final PerfumeTagRepository perfumeTagRepository;

    public ReviewService(ReviewRepository reviewRepository, PerfumeTagRepository perfumeTagRepository) {
        this.reviewRepository = reviewRepository;
        this.perfumeTagRepository = perfumeTagRepository;
    }

    public void saveReview(ReviewDto reviewDto) {

        try {
            Review review = new Review();
            review.setMemberId(reviewDto.getMemberId());
            review.setPerfumeId(reviewDto.getPerfumeId());
            review.setRating(reviewDto.getRating());
            review.setLongevity(reviewDto.getLongevity());
            review.setSillage(reviewDto.getSillage());
            review.setContent(reviewDto.getContent());
            review.setCreatedAt(LocalDateTime.now());

            reviewRepository.save(review);

            List<Long> ptagIds = reviewDto.getPtagIds();
            if (ptagIds != null && !ptagIds.isEmpty()) {
                for (Long ptagId: ptagIds) {
                    PerfumeTag perfumeTag = new PerfumeTag();
                    perfumeTag.setPerfumeId(reviewDto.getPerfumeId());
                    perfumeTag.setPtagId(ptagId);
                    perfumeTagRepository.save(perfumeTag);
                }
            }
        } catch (DataIntegrityViolationException e) {
            throw new IllegalStateException("이미 리뷰를 등록한 회원입니다.");
        }

    }

    public void deleteReview(Long reviewId) {
        reviewRepository.deleteById(reviewId);
    }
}
