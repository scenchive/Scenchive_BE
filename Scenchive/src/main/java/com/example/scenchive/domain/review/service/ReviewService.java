package com.example.scenchive.domain.review.service;

import com.example.scenchive.domain.filter.repository.Perfume;
import com.example.scenchive.domain.review.dto.PerfumeRatingDto;
import com.example.scenchive.domain.review.dto.ReviewListResponseDto;
import com.example.scenchive.domain.review.repository.RPerfumeTag;
import com.example.scenchive.domain.review.dto.ReviewDto;
import com.example.scenchive.domain.review.repository.RPerfumeTagRepository;
import com.example.scenchive.domain.review.repository.Review;
import com.example.scenchive.domain.review.repository.ReviewRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final RPerfumeTagRepository RPerfumeTagRepository;

    public ReviewService(ReviewRepository reviewRepository, RPerfumeTagRepository RPerfumeTagRepository) {
        this.reviewRepository = reviewRepository;
        this.RPerfumeTagRepository = RPerfumeTagRepository;
    }

    // 리뷰 등록 메서드
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
            String season = reviewDto.getSeason();
            Long seasonId = Long.parseLong(season);
            if (seasonId != null) {
                RPerfumeTag rPerfumeTag = new RPerfumeTag();
                rPerfumeTag.setPerfumeId(reviewDto.getPerfumeId());
                rPerfumeTag.setPtagId(seasonId);
                RPerfumeTagRepository.save(rPerfumeTag);
            }

        } catch (DataIntegrityViolationException e) {
            throw new IllegalStateException("이미 리뷰를 등록한 회원입니다.");
        }

    }

    // 리뷰 삭제 메서드
    public void deleteReview(Long reviewId) {
        reviewRepository.deleteById(reviewId);
    }


    // 개별 향수 리뷰 리스트 조회 메서드
    @Transactional(readOnly = true)
    public List<ReviewListResponseDto> findByPerfumeId(Long perfumeId){
//        Perfume reviewperfume = new Perfume(perfume_id);
        return reviewRepository.findByPerfumeId(perfumeId).stream()
                .map(perfume->new ReviewListResponseDto(perfume))
                .collect(Collectors.toList());
    }

    // 향수 평점 평균 산출 메서드
    public PerfumeRatingDto calculatePerfumeRating(Long perfumeId) {
        double ratingSum = reviewRepository.getRatingSumByPerfumeId(perfumeId);
        long reviewCount = reviewRepository.countByPerfumeId(perfumeId);
        double ratingAvg = reviewCount > 0 ? ratingSum / reviewCount : 0;

        double longevitySum = reviewRepository.getLongevitySumByPerfumeId(perfumeId);
        double longevityAvg = reviewCount > 0 ? longevitySum / reviewCount : 0;

        double sillageSum = reviewRepository.getSillageSumByPerfumeId(perfumeId);
        double sillageAvg = reviewCount > 0 ? sillageSum / reviewCount : 0;

        List<Object[]> seasonCounts = reviewRepository.getSeasonCountsByPerfumeId(perfumeId);
        Map<String, Double> seasonAvg = calculateSeasonAverages(seasonCounts, reviewCount);

        PerfumeRatingDto ratingDto = new PerfumeRatingDto();
        ratingDto.setPerfumeId(perfumeId);
        ratingDto.setRatingAvg(ratingAvg);
        ratingDto.setLongevityAvg(longevityAvg);
        ratingDto.setSillageAvg(sillageAvg);
        ratingDto.setSeasonAvg(seasonAvg);

        return ratingDto;
    }

    public Map<String, Double> calculateSeasonAverages(List<Object[]> seasonCounts, long reviewCount) {
//        List<Object[]> seasonCounts = reviewRepository.getSeasonCountsByPerfumeId(perfumeId);
        Map<String, Double> seasonAverages = new HashMap<>();

        // 총 투표수 계산
        int totalVotes = 0;
        for (Object[] seasonCount : seasonCounts) {
            Long count = (Long) seasonCount[1];
            totalVotes += count;
        }

        // 계절별 투표 비율 계산
        for (Object[] seasonCount : seasonCounts) {
            String season = (String) seasonCount[0];
            Long count = (Long) seasonCount[1];

            double percentage = Math.round((count.doubleValue() / totalVotes) * 100);
            seasonAverages.put(season, percentage);
        }

        return seasonAverages;
    }
}
