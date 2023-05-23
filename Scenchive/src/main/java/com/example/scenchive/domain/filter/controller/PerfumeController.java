package com.example.scenchive.domain.filter.controller;

import com.example.scenchive.domain.filter.dto.PerfumeDto;
import com.example.scenchive.domain.filter.service.PerfumeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/perfumes")
public class PerfumeController {
    private PerfumeService perfumeService;

    @Autowired
    public PerfumeController(PerfumeService perfumeService) {
        this.perfumeService = perfumeService;
    }

    @GetMapping("/recommend")
    public List<PerfumeDto> recommendPerfumes(@RequestParam("keywordId") Long keywordId) {
        // 유저가 선택한 키워드를 받아와 해당 키워드에 대한 향수 목록 조회
        List<PerfumeDto> recommendedPerfumes = perfumeService.getPerfumesByKeyword(keywordId);

        // 조회된 향수 목록 반환
        return recommendedPerfumes;
    }
}
