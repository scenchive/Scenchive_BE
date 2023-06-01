package com.example.scenchive.domain.member.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ResponseSurveyUserTagDto { //회원가입시 키워드 저장하고 나서 반환하는 Dto (확인용 값)
    private String name;

    private Long UtagId;

    public ResponseSurveyUserTagDto(String name, Long utagId) {
        this.name = name;
        UtagId = utagId;
    }
}
