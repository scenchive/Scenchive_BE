package com.example.scenchive.domain.member.service;

import com.example.scenchive.domain.member.dto.UtagDto;
import com.example.scenchive.domain.member.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
@Service
public class ProfileService {

    private final UserTagRepository userTagRepository;
    private final MemberRepository memberRepository;
    private final UtagRepository utagRepository;
    private final UtagTypeRepository utagTypeRepository;

    @Autowired
    public ProfileService(UserTagRepository userTagRepository, MemberRepository memberRepository, UtagRepository utagRepository, UtagTypeRepository utagTypeRepository) {
        this.userTagRepository = userTagRepository;
        this.memberRepository=memberRepository;
        this.utagRepository=utagRepository;
        this.utagTypeRepository=utagTypeRepository;
    }

    //향수 프로필 유저 키워드 조회
    @Transactional
    public List<UtagDto> profileGetUtags(Long userId){
        List<UtagDto> utagDtos = new ArrayList<>();
        Member member=memberRepository.findById(userId).get();
        List<UserTag> userTags = userTagRepository.findByMember(member);
        for(UserTag userTag : userTags){
            Long id=userTag.getUtag().getId();
            String utag=userTag.getUtag().getUtag();
            String utag_kr=userTag.getUtag().getUtag_kr();
            int utagtype_id=userTag.getUtag().getUtagtype().getId();

            UtagDto utagDto=new UtagDto(id, utag, utag_kr, utagtype_id);
            utagDtos.add(utagDto);
        }
        return utagDtos;
    }

    //향수 프로필 유저 키워드 저장
    @Transactional
    public String profileSave(Long userId, UtagDto utagDto){
        Member member=memberRepository.findById(userId).get();
        UtagType utagType=utagTypeRepository.findById(utagDto.getUtagtype_id()).get();

        Utag utag=Utag.builder().id(utagDto.getId())
                .utag(utagDto.getUtag())
                .utag_kr(utagDto.getUtag_kr())
                .utagtype(utagType)
                .build();

        //중복저장 방지
        Optional<UserTag> checkUserTag=userTagRepository.findByMemberAndUtag(member, utag);
        if(checkUserTag.isPresent()){
            return "already exists";
        }

        //db에 없는 경우에만 저장
        UserTag userTag=UserTag.builder()
                .member(member)
                .utag(utag)
                .build();

        userTagRepository.save(userTag);
        return "save";
    }

    //향수 프로필 유저 키워드 삭제
    @Transactional
    public String profileDelete(Long userId, UtagDto utagDto){
        Member member=memberRepository.findById(userId).get();
        Utag utag=utagRepository.findById(utagDto.getId()).get();
        userTagRepository.deleteByMemberAndUtag(member, utag);
        return "delete";
    }
}
