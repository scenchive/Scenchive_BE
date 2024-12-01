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

import java.util.List;
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

    private Member getCurrentMember() {
        return memberRepository.findByEmail(
                memberService.getMyUserWithAuthorities().getEmail()
        ).orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "사용자 정보를 찾을 수 없습니다."));
    }

}
