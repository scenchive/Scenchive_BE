package com.example.scenchive.domain.rank.service;

import com.example.scenchive.domain.filter.dto.SearchPerfumeDto;
import com.example.scenchive.domain.filter.repository.Brand;
import com.example.scenchive.domain.filter.repository.BrandRepository;
import com.example.scenchive.domain.filter.repository.Perfume;
import com.example.scenchive.domain.filter.repository.PerfumeRepository;
import com.example.scenchive.domain.rank.repository.*;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class PerfumeClickedService {

    static final int TOP_RANK_COUNT = 5;    // 클릭 수 상위 5개 향수

    private PerfumeClickedRepository perfumeClickedRepository;
    private PerfumeRepository perfumeRepository;
    private BrandRepository brandRepository;
    private SeasonRepository seasonRepository;

    // 향수 클릭 시 클릭 수 증가
    @Transactional
    public void updateClickCount(Long perfumeId, SeasonName seasonName) {
        Perfume perfume = perfumeRepository.findById(perfumeId).get();
        PerfumeClicked perfumeClicked = perfumeClickedRepository.findByPerfume(perfume);

        // 현재 계절 정보 가져오기
        Season season = seasonRepository.findByName(seasonName).get();

        if (perfumeClicked == null
                || perfumeClicked.getSeason().getName().getStartMonth() != season.getName().getStartMonth()) {
            perfumeClicked = new PerfumeClicked();
            perfumeClicked.setPerfume(perfume);
            perfumeClicked.setClickCount(1); // 초기값 설정
            perfumeClicked.setSeason(season);
            perfumeClickedRepository.save(perfumeClicked);
        } else {
            perfumeClicked.setClickCount(perfumeClicked.getClickCount() + 1);
        }
    }

    // 클릭 수 상위 5개 향수 반환
    public List<SearchPerfumeDto> getTopList(SeasonName seasonName) {
        Pageable top5 = PageRequest.of(0, TOP_RANK_COUNT); // 첫 번째 페이지, 5개 데이터

        // 현재 계절 정보 가져오기
        Season season = seasonRepository.findByName(seasonName).get();

        // 계절 정보에 맞는 인기 향수 목록
        List<PerfumeClickedMapping> topClickedPerfumes = perfumeClickedRepository.findBySeasonOrderByClickCountDesc(season, top5);

        // 결과 저장할 리스트
        List<SearchPerfumeDto> perfumeList = new ArrayList<>();

        // SearchPerfumeDto 생성
        for (PerfumeClickedMapping item : topClickedPerfumes) {
            Perfume perfume = item.getPerfume();

            // 향수 이미지
            String cleanedFileName = perfume.getPerfumeName().replaceAll("[^\\w]", "");
            String perfumeImage = "https://scenchive.s3.ap-northeast-2.amazonaws.com/perfume/" + cleanedFileName + ".jpg";

            // 브랜드 정보
            Brand brand = brandRepository.findById(perfume.getBrandId()).orElse(null);
            Long brandId = (brand != null) ? brand.getId() : null;
            String brandName = (brand != null) ? brand.getBrandName() : null;
            String brandName_kr = (brand != null) ? brand.getBrandName_kr() : null;
            String cleanedFileName2 = brand.getBrandName().replaceAll("[^\\w]", "");
            String brandImage = "https://scenchive.s3.ap-northeast-2.amazonaws.com/brand/" + cleanedFileName2 + ".jpg";

            perfumeList.add(new SearchPerfumeDto(perfume.getId(), perfume.getPerfumeName(), perfume.getPerfume_kr(), perfumeImage, brandId, brandName, brandName_kr, brandImage));
        }

        return perfumeList;
    }

}
