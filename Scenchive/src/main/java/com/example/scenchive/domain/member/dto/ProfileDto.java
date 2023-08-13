package com.example.scenchive.domain.member.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ProfileDto {

    private String email;

    private String name;

    public ProfileDto(String email, String name) {
        this.email = email;
        this.name = name;
    }
}
