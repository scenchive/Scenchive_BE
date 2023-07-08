package com.example.scenchive.domain.member.service;

import com.example.scenchive.domain.member.dto.MemberForm;
import com.example.scenchive.domain.member.exception.DuplicateMemberException;
import com.example.scenchive.domain.member.exception.NotFoundMemberException;
import com.example.scenchive.domain.member.repository.Authority;
import com.example.scenchive.domain.member.repository.Member;
import com.example.scenchive.domain.member.repository.MemberRepository;
import com.example.scenchive.domain.member.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
//

@Transactional(readOnly = true)
@Service
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public MemberService(MemberRepository memberRepository, PasswordEncoder passwordEncoder) {
        this.memberRepository = memberRepository;
        this.passwordEncoder = passwordEncoder;
    }

    //회원가입
//    @Transactional
//    public Long save(MemberForm memberForm){ //Id를 반환할지 member 객체 자체를 반환할지 고민
//        validDuplicateMember(memberForm);
//        return memberRepository.save(memberForm.toEntity()).getId();
//    }
//
//    //name 중복 확인
//    //검증 기능 동작함
//    private void validDuplicateMember(MemberForm memberForm){
//        Optional<Member> member=memberRepository.findByName(memberForm.getName());
//        if(member.isPresent()){
//            throw new IllegalStateException("이미 존재하는 닉네임입니다.");
//        }
//    }
    @Transactional
    public MemberForm signup(MemberForm memberForm) { // 회원가입 로직 수행

        // 이미 이 username으로 저장된 유저가 데이터베이스에 있는지
        if (memberRepository.findOneWithAuthoritiesByEmail(memberForm.getName()).orElse(null) != null) {
            throw new DuplicateMemberException("이미 가입되어 있는 유저입니다.");
        }

        // 권한정보 생성
        Authority authority = Authority.builder()
                .authorityName("ROLE_USER") // ROLE_USER 라는 권한
                .build();

        // 권한정보를 넣은 유저 생성
        Member member = Member.builder()
                .email(memberForm.getEmail())
                .name(memberForm.getName())
                .password(passwordEncoder.encode(memberForm.getPassword()))
                .authorities(Collections.singleton(authority))
                .activated(true)
                .build();

        return MemberForm.from(memberRepository.save(member));
    }

    @Transactional(readOnly = true)
    public MemberForm getUserWithAuthorities(String email) { // username을 기준으로 가져옴
        return MemberForm.from(memberRepository.findOneWithAuthoritiesByEmail(email).orElse(null));
    }

    @Transactional(readOnly = true)
    public MemberForm getMyUserWithAuthorities() { // 현재 SecurityContext에 저장되어있는 것만
        return MemberForm.from(
                SecurityUtil.getCurrentUsername()
                        .flatMap(memberRepository::findOneWithAuthoritiesByEmail)
                        .orElseThrow(() -> new NotFoundMemberException("Member not found"))
        );
    }
}