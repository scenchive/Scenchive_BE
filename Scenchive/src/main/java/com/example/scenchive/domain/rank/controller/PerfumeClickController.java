package com.example.scenchive.domain.rank.controller;

import com.example.scenchive.domain.filter.dto.SearchPerfumeDto;
import com.example.scenchive.domain.rank.repository.SeasonName;
import com.example.scenchive.domain.rank.service.PerfumeClickedService;
import com.example.scenchive.domain.rank.service.SeasonService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@RestController
@CrossOrigin(origins = {"http://localhost:3000", "https://scenchive.github.io/"}, allowCredentials = "true", allowedHeaders = "Authorization")
public class PerfumeClickController {

    private PerfumeClickedService perfumeClickedService;
    private SeasonService seasonService;

    // 계절별 클릭 수 top5 향수 리스트 반환
    @GetMapping("/main/popular-season")
    public List<SearchPerfumeDto> getPopularPerfumes() {
        int nowMonth = LocalDate.now().getMonthValue();
        SeasonName seasonName = seasonService.getSeasonName(nowMonth);
        return perfumeClickedService.getTopList(seasonName);
    }
}
