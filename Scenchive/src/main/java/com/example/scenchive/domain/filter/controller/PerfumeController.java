package com.example.scenchive.domain.filter.controller;

import com.example.scenchive.domain.filter.dto.*;
import com.example.scenchive.domain.filter.service.PersonalService;
import com.example.scenchive.domain.filter.service.SearchService;
import com.example.scenchive.domain.info.dto.NotesInfoDto;
import com.example.scenchive.domain.info.dto.NotesInfoResponse;
import com.example.scenchive.domain.info.repository.Perfumenote;
import com.example.scenchive.domain.info.service.NotesService;
import com.example.scenchive.domain.member.repository.MemberRepository;
import com.example.scenchive.domain.member.service.MemberService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import com.example.scenchive.domain.filter.service.PerfumeService;
import com.example.scenchive.domain.filter.repository.PTag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins="http://10.0.2.15:8081")
public class PerfumeController {
    private PerfumeService perfumeService;
    private PersonalService personalService;
    private SearchService searchService;
    private NotesService notesService;
    private MemberRepository memberRepository;
    private MemberService memberService;

    @Autowired
    public PerfumeController(PerfumeService perfumeService, PersonalService personalService,
                             SearchService searchService, NotesService notesService,
                             MemberRepository memberRepository, MemberService memberService) {
        this.perfumeService = perfumeService;
        this.personalService = personalService;
        this.searchService = searchService;
        this.notesService = notesService;
        this.memberRepository=memberRepository;
        this.memberService=memberService;
    }


    @GetMapping("/perfumes/recommend")
    public PerfumeResponseDto recommendPerfumes(@RequestParam("keywordId") List<PTag> keywordIds,
                                              @PageableDefault(size = 10) Pageable pageable) { // 향수 10개씩 반환
        // 유저가 선택한 키워드를 받아와 해당 키워드에 대한 향수 목록 조회
        List<PerfumeDto> recommendedPerfumes = perfumeService.getPerfumesByKeyword(keywordIds, pageable);
        // 키워드에 해당하는 향수의 총 개수 조회
        long totalPerfumeCount = perfumeService.getTotalPerfumeCount(keywordIds);

        PerfumeResponseDto responseDto = new PerfumeResponseDto(totalPerfumeCount, recommendedPerfumes);
        return responseDto;
    }

    //필터 추천 : 키워드 조회
    @GetMapping("/perfumes/recommend/type")
    List<PTagDto> getTypeKeyword(){
        return perfumeService.getTypeKeyword();
    }

    @GetMapping("/perfumes/recommend/tpo")
    List<PTagDto> getTPOKeyword(){
        return perfumeService.getTPOKeyword();
    }


    //향수 프로필 화면 : 넘겨받은 사용자 Id로 추천 향수 리스트 가져오기
//    @GetMapping("/profile/recommend")
//    public List<PersonalDto> personalRecommend(@RequestParam("userId") Long userId){
//        List<PersonalDto> personalRecommends =personalService.getPerfumesByUserKeyword(userId);
//        return personalRecommends;
//    }

    //향수 프로필 화면 : 토큰을 넘겨주면 추천 향수 리스트 가져오기
    @GetMapping("/profile/recommend")
    public List<PersonalDto> personalRecommend(){
        Long userId=memberRepository.findByEmail(memberService.getMyUserWithAuthorities().getEmail()).get().getId();
        List<PersonalDto> personalRecommends =personalService.getPerfumesByUserKeyword(userId);
        return personalRecommends;
    }

    //메인 화면 : 사용자 Id와 계절 Id를 넘겨주면 추천 향수 리스트를 반환
//    @GetMapping("recommend")
//    public List<PersonalDto> getPerfumesByUserAndSeason(@RequestParam("userId") Long userId, @RequestParam("season") Long seasonId){
//        List<PersonalDto> mainRecommends=personalService.getPerfumesByUserAndSeason(userId, seasonId);
//        return mainRecommends;
//    }

    //메인 화면 : 토큰과 계절 Id를 넘겨주면 추천 향수 리스트를 반환
    @GetMapping("recommend")
    public List<PersonalDto> getPerfumesByUserAndSeason(@RequestParam("season") Long seasonId){
        Long userId=memberRepository.findByEmail(memberService.getMyUserWithAuthorities().getEmail()).get().getId();
        List<PersonalDto> mainRecommends=personalService.getPerfumesByUserAndSeason(userId, seasonId);
        return mainRecommends;
    }


    //검색화면 : 향수 및 브랜드 조회
    @GetMapping("/search")
    public List<SearchPerfumeDto> searchName(@RequestParam("name") String name,
                                             @PageableDefault(size = 10) Pageable pageable){
        List<SearchPerfumeDto> searchDtos=searchService.searchName(name, pageable);
        return searchDtos;
    }

    //검색화면 : 브랜드별 향수 리스트 조회
    @GetMapping("/brandperfume")
    public BrandPerfumeResponseDto brandPerfume(@RequestParam("name") String name,
                                               @PageableDefault(size = 10) Pageable pageable){
        List<SearchPerfumeDto> brandDtos=searchService.brandPerfume(name, pageable);
        long totalBrandPerfumeCount = searchService.getTotalBrandPerfumeCount(name, pageable);

        BrandPerfumeResponseDto responseDto = new BrandPerfumeResponseDto(totalBrandPerfumeCount, brandDtos);
        return responseDto;
    }


    // 개별 향수 노트 정보 조회
    @GetMapping("/notesinfo/{perfumeId}")
    public ResponseEntity<NotesInfoResponse> getPerfumeNotesInfo(@PathVariable("perfumeId") Long perfumeId) {

        NotesInfoResponse notesInfoResponse = new NotesInfoResponse();
        notesInfoResponse.setPerfumeId(perfumeId);

        String perfumeName = notesService.getPerfumeNameByPerfumeId(perfumeId);
        notesInfoResponse.setPerfumeName(perfumeName);

        String brandName = notesService.getBrandNameByPerfumeId(perfumeId);
        notesInfoResponse.setBrandName(brandName);

        List<String> topNotes = notesService.getTopNotesByPerfumeId(perfumeId);
        notesInfoResponse.setTop(topNotes);

        List<String> middleNotes = notesService.getMiddleNotesByPerfumeId(perfumeId);
        notesInfoResponse.setMiddle(middleNotes);

        List<String> baseNotes = notesService.getBaseNotesByPerfumeId(perfumeId);
        notesInfoResponse.setBase(baseNotes);

        return ResponseEntity.ok(notesInfoResponse);
    }
}
