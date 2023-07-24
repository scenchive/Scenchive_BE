package com.example.scenchive.domain.member.service;

import com.example.scenchive.domain.member.repository.Member;
import com.example.scenchive.domain.member.repository.MemberRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component("userDetailsService")
public class CustomUserDetailsService implements UserDetailsService {
    private final MemberRepository memberRepository;

    public CustomUserDetailsService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    @Transactional // 두 개 이상의 쿼리가 실행될 때, atomic 실행을 보장하기 위한.
    public UserDetails loadUserByUsername(final String username) {
        return memberRepository.findOneWithAuthoritiesByEmail(username)
                .map(member -> createUser(username, member))
                .orElseThrow(() -> new UsernameNotFoundException(username + " -> 데이터베이스에서 찾을 수 없습니다."));
    }

    private org.springframework.security.core.userdetails.User createUser(String email, Member member) {
        if (!member.isActivated()) {
            throw new RuntimeException(email + " -> 활성화되어 있지 않습니다.");
        }

        // 유저의 권한을 모두 모아서, List 형태로 만듦
        List<GrantedAuthority> grantedAuthorities = member.getAuthorities().stream()
                .map(authority -> new SimpleGrantedAuthority(authority.getAuthorityName()))
                .collect(Collectors.toList());

        // 유저의 정보와 권한데이터를 통해 UserDetails 생성
        return new org.springframework.security.core.userdetails.User(member.getEmail(),
                member.getPassword(),
                grantedAuthorities);
    }
}