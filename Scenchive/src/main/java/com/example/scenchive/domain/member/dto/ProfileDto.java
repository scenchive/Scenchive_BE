package com.example.scenchive.domain.member.dto;

import com.example.scenchive.domain.member.repository.Member;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ProfileDto {
    private Long userId;

    private String email;

    private String name;

    private String imageUrl;

    public ProfileDto(Long userId, String email, String name, String imageUrl) {
        this.userId=userId;
        this.email = email;
        this.name = name;
        this.imageUrl=imageUrl;
    }

    public ProfileDto(Member entity) {
        this.userId=entity.getId();
        this.email = entity.getEmail();
        this.name = entity.getName();
        this.imageUrl=entity.getImageUrl();
    }
}
