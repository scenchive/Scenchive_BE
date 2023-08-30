package com.example.scenchive.domain.member.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ProfileDto {
    private Long userId;

    private String email;

    private String name;

    public ProfileDto(Long userId, String email, String name) {
        this.userId=userId;
        this.email = email;
        this.name = name;
    }
}
