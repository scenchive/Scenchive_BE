package com.example.scenchive.domain.filter.controller;

import com.example.scenchive.domain.filter.dto.PerfumeDto;
import com.example.scenchive.domain.filter.service.PerfumeService;
import com.example.scenchive.domain.filter.repository.PTag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class PerfumeController {
    private PerfumeService perfumeService;

    @Autowired
    public PerfumeController(PerfumeService perfumeService) {
        this.perfumeService = perfumeService;
    }

    @GetMapping("/perfumes/recommend")
    public List<PerfumeDto> recommendPerfumes(@RequestParam("keywordId") List<PTag> keywordIds) {
        // 유저가 선택한 키워드를 받아와 해당 키워드에 대한 향수 목록 조회
        List<PerfumeDto> recommendedPerfumes = perfumeService.getPerfumesByKeyword(keywordIds);

        // 조회된 향수 목록 반환
        return recommendedPerfumes;
    }
}
