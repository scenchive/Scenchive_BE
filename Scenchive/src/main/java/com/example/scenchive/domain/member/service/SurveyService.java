package com.example.scenchive.domain.member.service;

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

    @Autowired
    public SurveyService(UserTagRepository userTagRepository, UtagRepository utagRepository) {
        this.userTagRepository = userTagRepository;
        this.utagRepository = utagRepository;
    }


    @Transactional
    public List<UserTag> surveySave(SurveyUserTagDto surveyUserTagDto) {
        List<UserTag> userTags = surveyUserTagDto.toEntity();

        for (UserTag userTag : userTags) {
            if (!userTagRepository.existsByMemberAndUtag(userTag.getMember(), userTag.getUtag())) {
                userTagRepository.save(userTag);
            }
        }
        return userTags;
    }

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