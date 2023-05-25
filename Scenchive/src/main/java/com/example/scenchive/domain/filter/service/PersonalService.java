package com.example.scenchive.domain.filter.service;

import com.example.scenchive.domain.filter.dto.PerfumeDto;
import com.example.scenchive.domain.filter.repository.Perfume;
import com.example.scenchive.domain.filter.repository.PerfumeTag;
import com.example.scenchive.domain.filter.repository.PerfumeTagRepository;
import com.example.scenchive.domain.member.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Transactional(readOnly = true)
@Service
public class PersonalService {
    private final UserTagRepository userTagRepository;
    private final PerfumeTagRepository perfumeTagRepository;
    private final MemberRepository memberRepository;

    @Autowired
    public PersonalService(UserTagRepository userTagRepository, PerfumeTagRepository perfumeTagRepository, MemberRepository memberRepository){
        this.userTagRepository=userTagRepository;
        this.perfumeTagRepository=perfumeTagRepository;
        this.memberRepository=memberRepository;
    }


    //사용자 Id를 넘겨주면 추천 향수 리스트를 반환
    @Transactional
    public List<PerfumeDto> getPerfumesByUserKeyword(Long userId){
        Member member=memberRepository.findById(userId).get();
        List<UserTag> userTags=userTagRepository.findByMember(member);
        List<PerfumeDto> perfumes = new ArrayList<>();

        for (UserTag userTag:userTags){
            Long utagId=userTag.getUtag().getId();
            List<PerfumeTag> perfumeTags = perfumeTagRepository.findByPtagId(utagId);

            for (PerfumeTag perfumeTag : perfumeTags) {
                Perfume perfume = perfumeTag.getPerfume();
                PerfumeDto perfumeDto = new PerfumeDto(perfume.getId(), perfume.getPerfumeName());
                perfumes.add(perfumeDto);
            }
        }

        // 향수 DTO 리스트 반환
        return perfumes;
    }
}
