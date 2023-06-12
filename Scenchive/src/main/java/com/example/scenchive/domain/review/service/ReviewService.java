package com.example.scenchive.domain.review.service;

import com.example.scenchive.domain.review.repository.RPerfumeTag;
import com.example.scenchive.domain.review.dto.ReviewDto;
import com.example.scenchive.domain.review.repository.RPerfumeTagRepository;
import com.example.scenchive.domain.review.repository.Review;
import com.example.scenchive.domain.review.repository.ReviewRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final RPerfumeTagRepository RPerfumeTagRepository;

    public ReviewService(ReviewRepository reviewRepository, RPerfumeTagRepository RPerfumeTagRepository) {
        this.reviewRepository = reviewRepository;
        this.RPerfumeTagRepository = RPerfumeTagRepository;
    }

    public void saveReview(ReviewDto reviewDto) {

        try {
            Review review = new Review();
            review.setMemberId(reviewDto.getMemberId());
            review.setPerfumeId(reviewDto.getPerfumeId());
            review.setRating(reviewDto.getRating());
            review.setLongevity(reviewDto.getLongevity());
            review.setSillage(reviewDto.getSillage());
            review.setSeason(reviewDto.getSeason());
            review.setContent(reviewDto.getContent());
            review.setCreatedAt(LocalDateTime.now());

            reviewRepository.save(review);

            // 선택 키워드 저장
            List<Long> ptagIds = reviewDto.getPtagIds();
            if (ptagIds != null && !ptagIds.isEmpty()) {
                for (Long ptagId: ptagIds) {
                    RPerfumeTag RPerfumeTag = new RPerfumeTag();
                    RPerfumeTag.setPerfumeId(reviewDto.getPerfumeId());
                    RPerfumeTag.setPtagId(ptagId);
                    RPerfumeTagRepository.save(RPerfumeTag);
                }
            }

            // 계절감 평가 -> 키워드로 저장
            Long season = reviewDto.getSeason();
            if (season != null) {
                RPerfumeTag rPerfumeTag = new RPerfumeTag();
                rPerfumeTag.setPerfumeId(reviewDto.getPerfumeId());
                rPerfumeTag.setPtagId(season);
                RPerfumeTagRepository.save(rPerfumeTag);
            }

        } catch (DataIntegrityViolationException e) {
            throw new IllegalStateException("이미 리뷰를 등록한 회원입니다.");
        }

    }

    public void deleteReview(Long reviewId) {
        reviewRepository.deleteById(reviewId);
    }
}
