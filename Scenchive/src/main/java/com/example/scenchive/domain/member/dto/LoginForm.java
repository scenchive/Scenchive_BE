package com.example.scenchive.domain.member.dto;

import jakarta.validation.constraints.Email;
import lombok.*;

//
@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class LoginForm {
    @Email
    private String email;

    private String password;
}
