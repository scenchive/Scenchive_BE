package com.example.scenchive.service;

import com.example.scenchive.domain.member.service.MemberService;
import com.example.scenchive.domain.member.repository.MemberRepository;
import com.example.scenchive.domain.member.dto.MemberForm;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


/*@SpringBootTest
@Transactional
public class MemberServiceTest {
    @Autowired
    MemberService memberService;
    @Autowired
    MemberRepository memberRepository;
    @Test
    public void 회원가입() throws Exception {
        //Given
        MemberForm memberform = new MemberForm("jeong9032@naver.com", "kim", 1228);
        //When
        Long saveId =memberService.save(memberform);
        //Then
        assertEquals(saveId, memberRepository.findById(saveId).get().getId());
    }

    @Test
    public void 중복_회원_예외() throws Exception {
        //Given
        MemberForm member1 = new MemberForm("jeong9032@naver.com", "shin", 1228);
        MemberForm member2 = new MemberForm("jeong90232@naver.com", "shin", 12228);
        //When
        memberService.save(member1);
        //Then
        IllegalStateException thrown = assertThrows(IllegalStateException.class, () -> memberService.save(member2));
        assertEquals("이미 존재하는 닉네임입니다.", thrown.getMessage());
    }
}*/
