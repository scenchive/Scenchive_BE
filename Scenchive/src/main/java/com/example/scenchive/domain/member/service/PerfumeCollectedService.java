package com.example.scenchive.domain.member.service;

import com.example.scenchive.domain.filter.dto.BrandDto;
import com.example.scenchive.domain.filter.dto.PerfumeCollectedRequestDto;
import com.example.scenchive.domain.filter.dto.PerfumeCollectedResponseDto;
import com.example.scenchive.domain.filter.repository.*;
import com.example.scenchive.domain.filter.utils.BrandMapper;
import com.example.scenchive.domain.member.repository.Member;
import com.example.scenchive.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PerfumeCollectedService {

    private final PerfumeCollectedRepository perfumeCollectedRepository;
    private final BrandRepository brandRepository;
    private final MemberRepository memberRepository;
    private final MemberService memberService;
    private final PerfumeRepository perfumeRepository;

    // 보유 향수 추가
    public PerfumeCollectedResponseDto addPerfumeToCollection(PerfumeCollectedRequestDto perfumeCollectedRequestDto){
        // 인증된 사용자 정보
        Member currentMember = getCurrentMember();

        // 중복 확인
        if (perfumeCollectedRepository.findByMemberIdAndPerfumeId(getCurrentMember().getId(), perfumeCollectedRequestDto.getPerfumeId()).isPresent()) {
            throw new RuntimeException("이미 보유하고 있는 향수입니다.");
        }

        // perfumeId 유효 확인
        Perfume perfume = perfumeRepository.findById(perfumeCollectedRequestDto.getPerfumeId())
                .orElseThrow(() -> new RuntimeException("해당 향수가 존재하지 않습니다."));

        // 보유 향수 등록
        PerfumeCollected perfumeCollected = new PerfumeCollected();
        perfumeCollected.setMember(currentMember);
        perfumeCollected.setPerfume(perfume);

        PerfumeCollected savedPerfumeCollected = perfumeCollectedRepository.save(perfumeCollected);

        BrandDto brandDto = BrandMapper.toBrandDto(
                brandRepository.findById(perfume.getBrandId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "브랜드 정보를 찾을 수 없습니다."))
        );

        return new PerfumeCollectedResponseDto(
                savedPerfumeCollected.getId(),
                savedPerfumeCollected.getMember().getId(),
                savedPerfumeCollected.getPerfume().getId(),
                savedPerfumeCollected.getPerfume().getPerfumeName(),
                brandDto
        );
    }

    // 보유 향수 삭제
    @Transactional
    public void removePerfumeFromCollection(Long perfumeId){
        Member currentMember = getCurrentMember();

        // 보유 향수 확인하기
        PerfumeCollected perfumeCollected = perfumeCollectedRepository
                .findByMemberIdAndPerfumeId(currentMember.getId(), perfumeId)
                .orElseThrow(() -> new RuntimeException("해당 향수가 보유 목록에 없습니다."));

        // 삭제
        perfumeCollectedRepository.delete(perfumeCollected);
    }

    // 보유 향수 조회
    @Transactional(readOnly = true)
    public List<PerfumeCollectedResponseDto> getCollectedPerfumes(){
        Member currentMember = getCurrentMember();

        // 보유 향수 리스트 조회
        return perfumeCollectedRepository.findByMemberId(currentMember.getId())
                .stream().map(perfumeCollected -> {
                    BrandDto brandDto = BrandMapper.toBrandDto(
                            brandRepository.findById(perfumeCollected.getPerfume().getId())
                                    .orElseThrow(() -> new RuntimeException("브랜드 정보를 찾을 수 없습니다."))
                    );

                    return new PerfumeCollectedResponseDto(
                            perfumeCollected.getId(),
                            perfumeCollected.getMember().getId(),
                            perfumeCollected.getPerfume().getId(),
                            perfumeCollected.getPerfume().getPerfumeName(),
                            brandDto
                    );
                }).collect(Collectors.toList());
    }

    // 가장 많이 보유된 향수
    public Map<String, Object> getMostCollectedPerfume() {
        Object result = perfumeCollectedRepository.findTopCollectedPerfume();

        if (result == null) {
            throw new RuntimeException("가장 많이 보유된 향수가 없습니다.");
        }

        Object[] row = (Object[]) result;

        Long perfumeId = row[0] != null ? Long.valueOf(row[0].toString()) : null;
        String perfumeName = row[1] != null ? row[1].toString() : null;
        String perfumeKr = row[2] != null ? row[2].toString() : null;

        String cleanPerfumeName = (perfumeName != null) ? perfumeName.replaceAll("[^a-zA-Z0-9_]", "") : null;

        String perfumeImage = null;
        if (cleanPerfumeName != null) {
            perfumeImage = "https://scenchive.s3.ap-northeast-2.amazonaws.com/perfume/" + cleanPerfumeName + ".jpg";
        }

        //String perfumeImage = row[3] != null ? row[3].toString() : null;
        String brandName = row[4] != null ? row[4].toString() : null;
        String brandNameKr = row[5] != null ? row[5].toString() : null;

        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("perfumeId", perfumeId);
        resultMap.put("perfumeName", perfumeName);
        resultMap.put("perfume_kr", perfumeKr);
        resultMap.put("perfumeImage", perfumeImage);
        resultMap.put("brandName", brandName);
        resultMap.put("brandName_kr", brandNameKr);

        return resultMap;
    }

    // 가장 많이 보유한 향수 브랜드
    public Map<String, Object> getMostCollectedBrand() {
        Object result = perfumeCollectedRepository.findTopCollectedBrand();

        if (result == null) {
            throw new RuntimeException("가장 많이 보유된 향수 브랜드가 없습니다.");
        }
        Object[] row = (Object[]) result;

        Long brandId = row[0] != null ? Long.valueOf(row[0].toString()) : null;
        String brandName = row[1] != null ? row[1].toString() : null;
        String brandNameKr = row[2] != null ? row[2].toString() : null;
        //String brandImage = row[3] != null ? row[3].toString() : null;
        String cleanBrand = (brandName != null) ? brandName.replaceAll("[^\\w]", "") : null;

        String brandImage = null;

        if(cleanBrand != null){
            brandImage = "https://scenchive.s3.ap-northeast-2.amazonaws.com/brand/"+cleanBrand + ".jpg";
        }

        return Map.of(
                "brandId", brandId,
                "brandName", brandName,
                "brandName_kr", brandNameKr,
                "brandImage", brandImage
        );
    }

    // 유저 평균 향수 개수
    public Double getAveragePerfumeCount(){
        Double avgCount = perfumeCollectedRepository.findAveragePerfumeCount();
        if(avgCount == null){
            return 0.0;
        }
        return Math.round(avgCount*100)/100.0;
    }

    // 가장 많은 향수 보유 사용자
    public List<Map<String, Object>> getUserWithMostCollectedPerfumes(){
        List<Map<String, Object>> results = perfumeCollectedRepository.findUserWithMostCollectedPerfumes();
        if(results.isEmpty()){
            throw new RuntimeException("향수를 보유한 유저가 없습니다.");
        }
        return results;
    }

    private Member getCurrentMember() {
        return memberRepository.findByEmail(
                memberService.getMyUserWithAuthorities().getEmail()
        ).orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "사용자 정보를 찾을 수 없습니다."));
    }


}
