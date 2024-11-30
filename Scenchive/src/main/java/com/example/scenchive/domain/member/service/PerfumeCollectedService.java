package com.example.scenchive.domain.member.service;

import com.example.scenchive.domain.filter.dto.BrandDto;
import com.example.scenchive.domain.filter.dto.PerfumeCollectedRequestDto;
import com.example.scenchive.domain.filter.dto.PerfumeCollectedResponseDto;
import com.example.scenchive.domain.filter.repository.*;
import com.example.scenchive.domain.filter.utils.BrandMapper;
import com.example.scenchive.domain.member.repository.Member;
import com.example.scenchive.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PerfumeCollectedService {

    private final PerfumeCollectedRepository perfumeCollectedRepository;
    private final BrandRepository brandRepository;
    private final MemberRepository memberRepository;
    private final MemberService memberService;

    // 보유 향수 추가
    public PerfumeCollectedResponseDto addPerfumeToCollection(PerfumeCollectedRequestDto perfumeCollectedRequestDto){
        // 인증된 사용자 정보
        Member currentMember = getCurrentMember();

        // 중복 확인
        if(perfumeCollectedRepository.findByMemberIdAndPerfumeId(currentMember.getId(), perfumeCollectedRequestDto.getPerfumeId()).isPresent()){
            throw new RuntimeException("이미 보유하고 있는 향수입니다.");
        }

        Perfume perfume = new Perfume();
        perfume.setId(perfumeCollectedRequestDto.getPerfumeId());

        PerfumeCollected perfumeCollected = new PerfumeCollected();
        perfumeCollected.setMember(currentMember);
        perfumeCollected.setPerfume(perfume);

        PerfumeCollected savedPerfumeCollected = perfumeCollectedRepository.save(perfumeCollected);

        // brand
        BrandDto brandDto = BrandMapper.toBrandDto(
                brandRepository.findById(perfumeCollectedRequestDto.getPerfumeId()).orElseThrow(() -> new RuntimeException("브랜드 정보를 찾을 수 없습니다."))
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
    public void removePerfumeFromCollection(Long perfumeId){
        Member currentMember = getCurrentMember();

        // 보유 향수 확인하기
        PerfumeCollected perfumeCollected = perfumeCollectedRepository
                .findByMemberIdAndPerfumeId(currentMember.getId(), perfumeId)
                .orElseThrow(() -> new RuntimeException("해당 향수를 보유 목록에서 찾을 수 없습니다."));

        // 삭제
        perfumeCollectedRepository.delete(perfumeCollected);
    }

    // 보유 향수 조회
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

    private Member getCurrentMember(){
        return memberRepository.findByEmail(
                memberService.getMyUserWithAuthorities().getEmail()
        ).orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
    }

}
