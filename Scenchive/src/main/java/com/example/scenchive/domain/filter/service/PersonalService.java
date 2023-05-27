package com.example.scenchive.domain.filter.service;

import com.example.scenchive.domain.filter.dto.PerfumeDto;
import com.example.scenchive.domain.filter.dto.PersonalDto;
import com.example.scenchive.domain.filter.repository.*;
import com.example.scenchive.domain.member.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Transactional
@Service
public class PersonalService {
    private final UserTagRepository userTagRepository;
    private final PerfumeTagRepository perfumeTagRepository;
    private final MemberRepository memberRepository;
    private final BrandRepository brandRepository;
    private final PTagRepository pTagRepository;

    @Autowired
    public PersonalService(UserTagRepository userTagRepository, PerfumeTagRepository perfumeTagRepository, MemberRepository memberRepository, BrandRepository brandRepository, PTagRepository pTagRepository) {
        this.userTagRepository = userTagRepository;
        this.perfumeTagRepository = perfumeTagRepository;
        this.memberRepository = memberRepository;
        this.brandRepository=brandRepository;
        this.pTagRepository=pTagRepository;
    }

    //사용자 Id를 넘겨주면 추천 향수 리스트를 반환
    public List<PersonalDto> getPerfumesByUserKeyword(Long userId){
        Member member=memberRepository.findById(userId).get();
        List<UserTag> userTags=userTagRepository.findByMember(member);

        List<PersonalDto> perfumes = new ArrayList<>();
        List<Long> keywordIds=new ArrayList<>();

        for (UserTag userTag:userTags){
            Long utagId=userTag.getUtag().getId();
            keywordIds.add(utagId);
            PTag pTag=pTagRepository.findById(utagId).get();
            List<PerfumeTag> perfumeTags = perfumeTagRepository.findByPtag(pTag);

            for (PerfumeTag perfumeTag : perfumeTags) {
                Perfume perfume = perfumeTag.getPerfume();
                Brand brand=brandRepository.findById(perfume.getBrandId()).orElse(null);
                String brandName=(brand!=null)?brand.getBrandName():null;
                PersonalDto personalDto = new PersonalDto(perfume.getId(), perfume.getPerfumeName(), brandName, keywordIds);
                perfumes.add(personalDto);
            }
        }

        // 향수 DTO 리스트 반환
        return perfumes;
    }
}
