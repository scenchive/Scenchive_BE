package com.example.scenchive.domain.member.service;

import com.example.scenchive.domain.member.dto.SurveyUserTagDto;
import com.example.scenchive.domain.member.repository.Member;
import com.example.scenchive.domain.member.repository.UserTag;
import com.example.scenchive.domain.member.repository.UserTagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class SurveyService {
    private final UserTagRepository userTagRepository;

    @Transactional
    public List<UserTag> surveySave(SurveyUserTagDto surveyUserTagDto){
        List<UserTag> userTags=surveyUserTagDto.toEntity();

        for (UserTag userTag : userTags) {
            if (!userTagRepository.existsByMemberAndUtag(userTag.getMember(), userTag.getUtag())) {
                userTagRepository.save(userTag);
            }
        }
        return userTags;
    }
}
