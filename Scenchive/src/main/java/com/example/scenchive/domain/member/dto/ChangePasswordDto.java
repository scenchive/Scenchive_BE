package com.example.scenchive.domain.member.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ChangePasswordDto {

    @NotBlank
    private String currentPassword;

    @NotBlank
    private String newPassword;
}
