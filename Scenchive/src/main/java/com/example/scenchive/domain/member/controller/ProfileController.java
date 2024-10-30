package com.example.scenchive.domain.member.controller;

import com.example.scenchive.domain.member.dto.*;
import com.example.scenchive.domain.member.repository.MemberRepository;
import com.example.scenchive.domain.member.repository.UserTag;
import com.example.scenchive.domain.member.service.MemberService;
import com.example.scenchive.domain.member.service.ProfileService;
import com.example.scenchive.jwt.TokenProvider;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@CrossOrigin(origins = {"http://localhost:3000", "https://scenchive.github.io/"}, allowCredentials = "true", allowedHeaders = "Authorization")
public class ProfileController {
    private final MemberService memberService;
    private final ProfileService profileService;
    private final MemberRepository memberRepository;

    @Autowired
    public ProfileController(MemberService memberService, ProfileService profileService, MemberRepository memberRepository) {
        this.memberService = memberService;
        this.profileService = profileService;
        this.memberRepository=memberRepository;
    }

    //향수 프로필 화면 : 토큰을 넘겨주면 사용자 정보 조회
    @GetMapping("/profile")
    public ProfileDto getProfile(){
        Long userId=memberRepository.findByEmail(memberService.getMyUserWithAuthorities().getEmail()).get().getId();
        ProfileDto profileDto=profileService.getProfile(userId);
        return profileDto;
    }

    //향수 프로필 이미지 수정
    @PutMapping("/profile")
    public ProfileDto updateProfileImage(@RequestPart(required = false) MultipartFile image){
        Long userId=memberRepository.findByEmail(memberService.getMyUserWithAuthorities().getEmail()).get().getId();
        ProfileDto profileDto=profileService.updateImage(userId, image);
        return profileDto;
    }

    //향수 프로필 이미지 삭제
    @DeleteMapping("/profile")
    public String deleteProfileImage(){
        Long userId=memberRepository.findByEmail(memberService.getMyUserWithAuthorities().getEmail()).get().getId();
        profileService.deleteImage(userId);
        return "프로필 이미지 삭제 완료";
    }

    //토큰을 넘겨주면 향수 프로필 유저 키워드 조회
    @GetMapping("/keyword")
    public List<UtagDto> profileGetUtags(){
        Long userId=memberRepository.findByEmail(memberService.getMyUserWithAuthorities().getEmail()).get().getId();
        List<UtagDto> utagDtos=profileService.profileGetUtags(userId);
        return utagDtos;
    }

    //토큰을 넘겨주면 향수 프로필 유저 키워드 수정 (저장 및 삭제)
    @PutMapping("/keyword")
    public String profileKeywordEdit(@RequestBody List<UtagDto> utagDtoList){
        Long userId=memberRepository.findByEmail(memberService.getMyUserWithAuthorities().getEmail()).get().getId();
        return profileService.profileKeywordEdit(userId, utagDtoList);
    }

    //토큰을 넘겨주면 향수 프로필 유저 닉네임 수정
    @PutMapping("/member/name")
    public String changeName(@Valid @RequestBody CheckNameDto checkNameDto){
        Long userId=memberRepository.findByEmail(memberService.getMyUserWithAuthorities().getEmail()).get().getId();
        return profileService.changeName(userId, checkNameDto);
    }

    @PutMapping("/member/password")
    public String changePassword(@Valid @RequestBody ChangePasswordDto changePasswordDto) {
        Long userId=memberRepository.findByEmail(memberService.getMyUserWithAuthorities().getEmail()).get().getId();
        return profileService.changePassword(userId, changePasswordDto);
    }
}
