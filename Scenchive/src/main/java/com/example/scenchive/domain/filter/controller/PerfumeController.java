package com.example.scenchive.domain.filter.controller;

import com.example.scenchive.domain.filter.dto.PerfumeDto;
import com.example.scenchive.domain.filter.dto.PersonalDto;
import com.example.scenchive.domain.filter.dto.SearchPerfumeDto;
import com.example.scenchive.domain.filter.repository.PTag;
import com.example.scenchive.domain.filter.service.PerfumeService;
import com.example.scenchive.domain.filter.service.PersonalService;
import com.example.scenchive.domain.filter.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class PerfumeController {
    private PerfumeService perfumeService;
    private PersonalService personalService;
    private SearchService searchService;

    @Autowired
    public PerfumeController(PerfumeService perfumeService, PersonalService personalService, SearchService searchService) {
        this.perfumeService = perfumeService;
        this.personalService = personalService;
        this.searchService=searchService;
    }


    @GetMapping("/perfumes/recommend")
    public List<PerfumeDto> recommendPerfumes(@RequestParam("keywordId") List<PTag> keywordIds) {
        // 유저가 선택한 키워드를 받아와 해당 키워드에 대한 향수 목록 조회
        List<PerfumeDto> recommendedPerfumes = perfumeService.getPerfumesByKeyword(keywordIds);

        // 조회된 향수 목록 반환
        return recommendedPerfumes;
    }


    //향수 프로필 화면 : 넘겨받은 사용자 Id로 추천 향수 리스트 가져오기
    @GetMapping("/profile/recommend")
    public List<PersonalDto> personalRecommend(@RequestParam("userId") Long userId){
        List<PersonalDto> personalRecommends =personalService.getPerfumesByUserKeyword(userId);
        return personalRecommends;
    }

    //메인 화면 : 사용자 Id와 계절 Id를 넘겨주면 추천 향수 리스트를 반환
    @GetMapping("recommend")
    public List<PersonalDto> getPerfumesByUserAndSeason(@RequestParam("userId") Long userId, @RequestParam("season") Long seasonId){
        List<PersonalDto> mainRecommends=personalService.getPerfumesByUserAndSeason(userId, seasonId);
        return mainRecommends;
    }


    //검색화면 : 향수 및 브랜드 조회
    @GetMapping("/search")
    public List<SearchPerfumeDto> searchName(@RequestParam("name") String name){
        List<SearchPerfumeDto> searchDtos=searchService.searchName(name);
        return searchDtos;
    }
}
