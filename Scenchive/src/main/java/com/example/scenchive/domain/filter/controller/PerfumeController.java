package com.example.scenchive.domain.filter.controller;

import com.example.scenchive.domain.filter.dto.PerfumeDto;
import com.example.scenchive.domain.filter.dto.PersonalDto;
import com.example.scenchive.domain.filter.repository.PTag;
import com.example.scenchive.domain.filter.service.PerfumeService;
import com.example.scenchive.domain.filter.service.PersonalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class PerfumeController {
    private PerfumeService perfumeService;
    private PersonalService personalService;

    @Autowired
    public PerfumeController(PerfumeService perfumeService, PersonalService personalService) {
        this.perfumeService = perfumeService;
        this.personalService = personalService;
    }


    @GetMapping("/perfumes/recommend")
    public List<PerfumeDto> recommendPerfumes(@RequestParam("keywordId") List<PTag> keywordIds) {
        // 유저가 선택한 키워드를 받아와 해당 키워드에 대한 향수 목록 조회
        List<PerfumeDto> recommendedPerfumes = perfumeService.getPerfumesByKeyword(keywordIds);

        // 조회된 향수 목록 반환
        return recommendedPerfumes;
    }


//    //넘겨받은 사용자 Id로 추천 향수 리스트 가져오기
    @GetMapping("/profile/recommend")
    public List<PersonalDto> personalRecommend(@RequestParam("userId") Long userId){
        List<PersonalDto> personalRecommends =personalService.getPerfumesByUserKeyword(userId);
        return personalRecommends;
    }
}
