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

}
