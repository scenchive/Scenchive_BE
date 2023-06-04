package com.example.scenchive.domain.member.controller;

import com.example.scenchive.domain.member.dto.UtagDto;
import com.example.scenchive.domain.member.repository.UserTag;
import com.example.scenchive.domain.member.service.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class ProfileController {
    private final ProfileService profileService;

    //향수 프로필 유저 키워드 조회
    @GetMapping("/keyword/{userId}")
    public List<UtagDto> profileGetUtags(@PathVariable Long userId){
        List<UtagDto> utagDtos=profileService.profileGetUtags(userId);
        return utagDtos;
    }

    //향수 프로필 유저 키워드 저장
    @PostMapping("/keyword/{userId}")
    public String profileSave(@PathVariable Long userId, @RequestBody UtagDto utagDto){
        return profileService.profileSave(userId, utagDto);
    }

    //향수 프로필 유저 키워드 삭제
    @DeleteMapping("/keyword/{userId}")
    public String profileDelete(@PathVariable Long userId, @RequestBody UtagDto utagDto){
        return profileService.profileDelete(userId, utagDto);
    }
}
