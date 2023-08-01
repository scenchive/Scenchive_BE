package com.example.scenchive.domain.member.controller;

import com.example.scenchive.domain.member.dto.ProfileDto;
import com.example.scenchive.domain.member.dto.UtagDto;
import com.example.scenchive.domain.member.repository.MemberRepository;
import com.example.scenchive.domain.member.repository.UserTag;
import com.example.scenchive.domain.member.service.MemberService;
import com.example.scenchive.domain.member.service.ProfileService;
import com.example.scenchive.jwt.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.List;

@RestController
@CrossOrigin(origins="http://10.0.2.15:8081")
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

    //향수 프로필 유저 키워드 조회
//    @GetMapping("/keyword/{userId}")
//    public List<UtagDto> profileGetUtags(@PathVariable Long userId){
//        List<UtagDto> utagDtos=profileService.profileGetUtags(userId);
//        return utagDtos;
//    }

    //토큰을 넘겨주면 향수 프로필 유저 키워드 조회
    @GetMapping("/keyword")
    public List<UtagDto> profileGetUtags(){
        Long userId=memberRepository.findByEmail(memberService.getMyUserWithAuthorities().getEmail()).get().getId();
        List<UtagDto> utagDtos=profileService.profileGetUtags(userId);
        return utagDtos;
    }

    //향수 프로필 유저 키워드 수정 (저장 및 삭제)
//    @PutMapping("/keyword/{userId}")
//    public String profileKeywordEdit(@PathVariable Long userId, @RequestBody List<UtagDto> utagDtoList){
//        return profileService.profileKeywordEdit(userId, utagDtoList);
//    }

    //토큰을 넘겨주면 향수 프로필 유저 키워드 수정 (저장 및 삭제)
    @PutMapping("/keyword")
    public String profileKeywordEdit(@RequestBody List<UtagDto> utagDtoList){
        Long userId=memberRepository.findByEmail(memberService.getMyUserWithAuthorities().getEmail()).get().getId();
        return profileService.profileKeywordEdit(userId, utagDtoList);
    }

    //향수 프로필 수정하기 클릭 시 키워드 조회
//    @GetMapping("/allkeyword/{userId}")
//    public List<List> profileGetAllTags(@PathVariable Long userId){
//        List<List> utagDtos=profileService.profileGetAllTags(userId);
//        return utagDtos;
//    }

//    //향수 프로필 유저 키워드 저장
//    @PostMapping("/keyword/{userId}")
//    public String profileSave(@PathVariable Long userId, @RequestBody UtagDto utagDto){
//        return profileService.profileSave(userId, utagDto);
//    }
//
//    //향수 프로필 유저 키워드 삭제
//    @DeleteMapping("/keyword/{userId}")
//    public String profileDelete(@PathVariable Long userId, @RequestBody UtagDto utagDto){
//        return profileService.profileDelete(userId, utagDto);
//    }
}
