package com.example.scenchive.domain.member.service;

import com.example.scenchive.domain.member.dto.ResponseSurveyUserTagDto;
import com.example.scenchive.domain.member.dto.SurveyUserTagDto;
import com.example.scenchive.domain.member.dto.UtagDto;
import com.example.scenchive.domain.member.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Transactional(readOnly = true)
@Service
public class SurveyService {
    private final UserTagRepository userTagRepository;
    private final UtagRepository utagRepository;
    private final MemberRepository memberRepository;

    @Autowired
    public SurveyService(UserTagRepository userTagRepository, UtagRepository utagRepository, MemberRepository memberRepository) {
        this.userTagRepository = userTagRepository;
        this.utagRepository = utagRepository;
        this.memberRepository=memberRepository;
    }


    //회원가입시 입력한 키워드 저장하기
    @Transactional
    public List<ResponseSurveyUserTagDto> surveySave(SurveyUserTagDto surveyUserTagDto) {
        Member member=memberRepository.findByName(surveyUserTagDto.getName()).get();
        List<UserTag> userTags = surveyUserTagDto.toEntity(member);
        List<ResponseSurveyUserTagDto> responseSurveyUserTagDtos=new ArrayList<>();

        for (UserTag userTag : userTags) {
            if (!userTagRepository.existsByMemberAndUtag(userTag.getMember(), userTag.getUtag())) {
                userTagRepository.save(userTag); //DB에 userTag 저장
                Long utagId=userTag.getUtag().getId();
                ResponseSurveyUserTagDto responseSurveyUserTagDto=new ResponseSurveyUserTagDto(surveyUserTagDto.getName(), utagId);
                responseSurveyUserTagDtos.add(responseSurveyUserTagDto);
            }
        }
        return responseSurveyUserTagDtos;
    }

    //회원가입 설문조사 키워드 조회
    @Transactional(readOnly = true)
    public List<UtagDto> surveyGetUtags() {
        List<UtagDto> utagDtos = new ArrayList<>();
        for (long i = 1; i <= 25; i++) {
            UtagDto utagDto = new UtagDto(i,
                    utagRepository.findById(i).get().getUtag(),
                    utagRepository.findById(i).get().getUtag_kr(),
                    utagRepository.findById(i).get().getUtagtype().getId());
            utagDtos.add(utagDto);
        }
        return utagDtos;
    }
}