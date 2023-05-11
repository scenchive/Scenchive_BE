package com.example.scenchive.domain.member.dto;

import com.example.scenchive.domain.member.repository.Member;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MemberForm {
    @Email
    private String email;
    @NotBlank
    private String name;

    private int password;

    @Builder
    public MemberForm(String email, String name, int password) {
        this.email = email;
        this.name = name;
        this.password = password;
    }

    public Member toEntity(){
        return Member.builder()
                .email(email)
                .name(name)
                .password(password)
                .build();
    }
}
