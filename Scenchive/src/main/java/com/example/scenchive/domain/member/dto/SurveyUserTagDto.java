package com.example.scenchive.domain.member.dto;

import com.example.scenchive.domain.member.repository.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
public class SurveyUserTagDto {
    private String name;

    private List<Utag> utags;

    public SurveyUserTagDto(String name, List<Utag> utags) {
        this.name = name;
        this.utags = utags;
    }

    //
//    @Builder
//    public SurveyUserTagDto(Member member, List<Utag> utags) {
//        this.member = member;
//        this.utags = utags;
//    }

    public List<UserTag> toEntity(Member member){
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
