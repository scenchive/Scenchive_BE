package com.example.scenchive.domain.member.dto;

import com.example.scenchive.domain.member.repository.Member;
import com.example.scenchive.domain.member.repository.UserTag;
import com.example.scenchive.domain.member.repository.Utag;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
public class SurveyUserTagDto {
    private Member member;

    private List<Utag> utags;

//
//    @Builder
//    public SurveyUserTagDto(Member member, List<Utag> utags) {
//        this.member = member;
//        this.utags = utags;
//    }

    public List<UserTag> toEntity(){
        List<UserTag> userTags=new ArrayList<>();
        for (Utag utag : utags){
            userTags.add(UserTag.builder()
                    .member(member)
                    .utag(utag)
                    .build());
        }
        return userTags;
    }
}
