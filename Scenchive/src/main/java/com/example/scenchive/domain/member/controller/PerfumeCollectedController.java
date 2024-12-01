package com.example.scenchive.domain.member.controller;

import com.amazonaws.Response;
import com.example.scenchive.domain.filter.dto.PerfumeCollectedRequestDto;
import com.example.scenchive.domain.filter.dto.PerfumeCollectedResponseDto;
import com.example.scenchive.domain.filter.repository.BrandRepository;
import com.example.scenchive.domain.member.repository.MemberRepository;
import com.example.scenchive.domain.member.service.MemberService;
import com.example.scenchive.domain.member.service.PerfumeCollectedService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class PerfumeCollectedController {

    private final PerfumeCollectedService perfumeCollectedService;

    // 사용자 보유 향수 추가
    @PostMapping("/user/perfume-collect")
    public ResponseEntity<String> addPerfumeToCollection(@RequestBody PerfumeCollectedRequestDto perfumeCollectedRequestDto){
        try{
            PerfumeCollectedResponseDto perfumeCollectedResponseDto = perfumeCollectedService.addPerfumeToCollection(perfumeCollectedRequestDto);
            return ResponseEntity.status(HttpStatus.CREATED).body("향수가 보유 목록에 추가되었습니다.");
        } catch(RuntimeException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/user/perfume-collect")
    public ResponseEntity<?> getCollectedPerfumes(){
        List<PerfumeCollectedResponseDto> responseDtos = perfumeCollectedService.getCollectedPerfumes();

        if(responseDtos.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("보유한 향수가 없습니다.");
        }

        return ResponseEntity.ok(responseDtos);
    }

    @DeleteMapping("/user/perfume-collect/{perfumeId}")
    public ResponseEntity<String> removePerfumeFromCollection(@PathVariable Long perfumeId){
        try{
            perfumeCollectedService.removePerfumeFromCollection(perfumeId);
            return ResponseEntity.ok("향수가 보유 목록에서 삭제되었습니다.");
        } catch(RuntimeException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

}
