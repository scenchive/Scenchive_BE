package com.example.scenchive.domain.member.controller;

import com.example.scenchive.domain.filter.dto.PerfumeCollectedRequestDto;
import com.example.scenchive.domain.filter.dto.PerfumeCollectedResponseDto;
import com.example.scenchive.domain.filter.repository.BrandRepository;
import com.example.scenchive.domain.member.repository.MemberRepository;
import com.example.scenchive.domain.member.service.MemberService;
import com.example.scenchive.domain.member.service.PerfumeCollectedService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class PerfumeCollectedController {

    private final PerfumeCollectedService perfumeCollectedService;

    @PostMapping("/user/perfume-collect")
    public ResponseEntity<String> addPerfumeToCollection(@RequestBody PerfumeCollectedRequestDto perfumeCollectedRequestDto){
        PerfumeCollectedResponseDto perfumeCollectedResponseDto = perfumeCollectedService.addPerfumeToCollection(perfumeCollectedRequestDto);
        return ResponseEntity.ok("향수가 보유 목록에 추가되었습니다.");
    }

    @GetMapping("/user/perfume-collect")
    public ResponseEntity<List<PerfumeCollectedResponseDto>> getCollectedPerfumes(){
        List<PerfumeCollectedResponseDto> responseDtos = perfumeCollectedService.getCollectedPerfumes();
        return ResponseEntity.ok(responseDtos);
    }

    @DeleteMapping("/user/perfume-collect/{perfumeId}")
    public ResponseEntity<String> removePerfumeFromCollection(@PathVariable Long perfumeId){
        perfumeCollectedService.removePerfumeFromCollection(perfumeId);
        return ResponseEntity.ok("향수가 보유 목록에서 삭제되었습니다.");
    }

}
