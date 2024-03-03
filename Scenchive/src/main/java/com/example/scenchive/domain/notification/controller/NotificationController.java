package com.example.scenchive.domain.notification.controller;

import com.example.scenchive.domain.board.dto.BoardSaveRequestDto;
import com.example.scenchive.domain.member.repository.Member;
import com.example.scenchive.domain.member.repository.MemberRepository;
import com.example.scenchive.domain.member.service.MemberService;
import com.example.scenchive.domain.notification.dto.NotificationDto;
import com.example.scenchive.domain.notification.dto.ResponseNotificationDto;
import com.example.scenchive.domain.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
public class NotificationController {
    private final NotificationService notificationService;
    private final MemberRepository memberRepository;
    private final MemberService memberService;

    @Autowired
    public NotificationController(NotificationService notificationService, MemberRepository memberRepository,
                                  MemberService memberService) {
        this.notificationService = notificationService;
        this.memberRepository = memberRepository;
        this.memberService=memberService;
    }

    //유저별 알림 목록 조회
    @GetMapping("/notification")
    public ResponseNotificationDto getNotifications(@PageableDefault(size=10) Pageable pageable) {
        Member member = memberRepository.findByEmail(memberService.getMyUserWithAuthorities().getEmail()).get();
        return notificationService.getNotifications(pageable, member);
    }

    //알림 클릭
    @PostMapping("/notification/{id}")
    public void readNotification(@PathVariable("id") Long id){
        notificationService.readNotification(id);
    }

    //알림 확인 유무
    @GetMapping("/readNotification/{id}")
    public String checkBoolean(@PathVariable("id") Long id){
        return notificationService.checkBoolean(id);
    }

    //일주일 지난 알림 삭제
    @PostMapping("/oldNotification")
    public void deleteOldNotifications(){
        notificationService.deleteOldNotifications();
    }

}
