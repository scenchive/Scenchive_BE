package com.example.scenchive.member.dto;

import jakarta.validation.constraints.Email;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
//
@Getter
@Setter
@NoArgsConstructor
public class LoginForm {
    @Email
    private String email;

    private int password;
}
