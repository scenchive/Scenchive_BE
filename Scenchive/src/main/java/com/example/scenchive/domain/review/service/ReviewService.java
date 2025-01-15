package com.example.scenchive.domain.review.service;

import com.example.scenchive.domain.filter.repository.Brand;
import com.example.scenchive.domain.filter.repository.BrandRepository;
import com.example.scenchive.domain.filter.repository.Perfume;
import com.example.scenchive.domain.filter.repository.PerfumeRepository;
import com.example.scenchive.domain.member.repository.Member;
import com.example.scenchive.domain.member.repository.MemberRepository;
import com.example.scenchive.domain.member.service.MemberService;
import com.example.scenchive.domain.review.dto.PerfumeRatingDto;
import com.example.scenchive.domain.review.dto.PerfumeDetailsDto;
import com.example.scenchive.domain.review.dto.ReviewListResponseDto;
import com.example.scenchive.domain.review.repository.RPerfumeTag;
import com.example.scenchive.domain.review.dto.ReviewDto;
import com.example.scenchive.domain.review.repository.RPerfumeTagRepository;
import com.example.scenchive.domain.review.repository.Review;
import com.example.scenchive.domain.review.repository.ReviewRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final RPerfumeTagRepository RPerfumeTagRepository;
    private final MemberRepository memberRepository;
    private final MemberService memberService;
    private final PerfumeRepository perfumeRepository;
    private final BrandRepository brandRepository;

    public ReviewService(ReviewRepository reviewRepository, RPerfumeTagRepository RPerfumeTagRepository,
                         MemberRepository memberRepository, MemberService memberService, PerfumeRepository perfumeRepository,
                         BrandRepository brandRepository) {
        this.reviewRepository = reviewRepository;
        this.RPerfumeTagRepository = RPerfumeTagRepository;
        this.memberRepository=memberRepository;
        this.memberService=memberService;
        this.perfumeRepository = perfumeRepository;
        this.brandRepository = brandRepository;
    }

    // 리뷰 많은 향수 top 5
    public List<PerfumeDetailsDto> getTop5PerfumesByReviews(){
        List<Object[]> results = reviewRepository.getTop5PerfumesReviewCount(PageRequest.of(0, 5));
        return results.stream().map(result -> {
                    Long perfumeId = (Long) result[0];
                    Perfume perfume = perfumeRepository.findById(perfumeId)
                            .orElseThrow(() -> new IllegalArgumentException("향수 정보를 찾을 수 없습니다. ID: " + perfumeId));
                    Brand brand = brandRepository.findById(perfume.getBrandId())
                            .orElseThrow(() -> new IllegalArgumentException("브랜드 정보를 찾을 수 없습니다. ID: " + perfume.getBrandId()));

                    String cleanedFileName = perfume.getPerfumeName().replaceAll("[^\\w]", "");
                    String perfumeImage = "https://scenchive.s3.ap-northeast-2.amazonaws.com/perfume/" + cleanedFileName + ".jpg";

                    return new PerfumeDetailsDto(
                            perfume.getId(),
                            brand.getBrandName(),
                            brand.getBrandName_kr(),
                            perfumeImage,
                            perfume.getPerfumeName(),
                            perfume.getPerfume_kr()
                    );
                }).collect(Collectors.toList());
    }

//    public List<PerfumeDetailsByReviewDto> getTop5PerfumesByReviews(){
//        List<Object[]> results = reviewRepository.getTop5PerfumesReviewCount(PageRequest.of(0, 5));
//        return results.stream()
//                .map(result -> {
//                    Long perfumeId = (Long) result[0];
//                    long reviewCount = ((Number) result[1]).longValue();
//
//                    // Perfume 정보
//                    Perfume perfume = perfumeRepository.findById(perfumeId).orElseThrow(()-> new IllegalArgumentException("향수 정보를 찾을 수 없습니다. ID : "+perfumeId));
//                    Brand brand = brandRepository.findById(perfume.getBrandId()).orElseThrow(()-> new IllegalArgumentException("브랜드 정보를 찾을 수 없습니다. ID: "+ perfume.getBrandId()));
//
//                    return new PerfumeDetailsByReviewDto(
//                            perfume.getId(),
//                            perfume.getPerfumeName(),
//                            brand.getBrandName(),
//                            reviewCount
//                    );
//                }).collect(Collectors.toList());
//    }

    // 리뷰 등록 메서드
    public void saveReview(ReviewDto reviewDto) {

        try {
            Review review = new Review();
            Member userId=memberRepository.findByEmail(memberService.getMyUserWithAuthorities().getEmail()).get();
            review.setMemberId(userId);
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
        return reviewRepository.findByPerfumeIdOrderByCreatedAtDesc(perfumeId).stream()
                .map(perfume->new ReviewListResponseDto(perfume))
                .collect(Collectors.toList());
    }

    // 향수 평점 평균 산출 메서드
    public PerfumeRatingDto calculatePerfumeRating(Long perfumeId) {

        if (reviewRepository.findByPerfumeIdOrderByCreatedAtDesc(perfumeId).size()!=0){
            double ratingSum = reviewRepository.getRatingSumByPerfumeId(perfumeId);
            long reviewCount = reviewRepository.countByPerfumeId(perfumeId);
            double ratingAvg = reviewCount > 0 ? ratingSum / reviewCount : 0;
            ratingAvg = Math.round(ratingAvg * 10.0) / 10.0;

            double longevitySum = reviewRepository.getLongevitySumByPerfumeId(perfumeId);
            double longevityAvg = reviewCount > 0 ? longevitySum / reviewCount : 0;
            longevityAvg = Math.round(longevityAvg * 10.0) / 10.0;

            double sillageSum = reviewRepository.getSillageSumByPerfumeId(perfumeId);
            double sillageAvg = reviewCount > 0 ? sillageSum / reviewCount : 0;
            sillageAvg = Math.round(sillageAvg * 10.0) / 10.0;

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

        else{
            double ratingAvg =0.0;
            double longevityAvg =0.0;
            double sillageAvg =0.0;
            Map<String, Double> seasonAvg = new HashMap<>();
            seasonAvg.put("spring", 0.0);
            seasonAvg.put("summer", 0.0);
            seasonAvg.put("fall", 0.0);
            seasonAvg.put("winter", 0.0);

            PerfumeRatingDto ratingDto = new PerfumeRatingDto();
            ratingDto.setPerfumeId(perfumeId);
            ratingDto.setRatingAvg(ratingAvg);
            ratingDto.setLongevityAvg(longevityAvg);
            ratingDto.setSillageAvg(sillageAvg);
            ratingDto.setSeasonAvg(seasonAvg);

            return ratingDto;
        }
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
            String season = convertSeasonToString((String) seasonCount[0]);
            Long count = (Long) seasonCount[1];

            double percentage = Math.round((count.doubleValue() / totalVotes) * 100);
            seasonAverages.put(season, percentage);
        }

        // 모든 계절에 대한 투표수의 default를 0.0으로 설정
        String[] allSeasons = {"spring", "summer", "fall", "winter"};
        for (String season : allSeasons) {
            if (!seasonAverages.containsKey(season)) {
                seasonAverages.put(season, 0.0);
            }
        }

        // response에서 봄-여름-가을-겨울 순으로 표시
        Map<String, Double> sortedSeasonAverages = new LinkedHashMap<>();
        sortedSeasonAverages.put("spring", seasonAverages.get("spring"));
        sortedSeasonAverages.put("summer", seasonAverages.get("summer"));
        sortedSeasonAverages.put("fall", seasonAverages.get("fall"));
        sortedSeasonAverages.put("winter", seasonAverages.get("winter"));

        return sortedSeasonAverages;
    }

    // 숫자로 표현된 계절을 문자열로 변환
    private String convertSeasonToString(String season) {
        switch (season) {
            case "36":
                return "spring";
            case "37":
                return "summer";
            case "38":
                return "fall";
            case "39":
                return "winter";
            default:
                return "unknown";
        }
    }
}
