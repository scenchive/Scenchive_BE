package com.example.scenchive.domain.notification.service;

import com.example.scenchive.domain.member.repository.Member;
import com.example.scenchive.domain.member.repository.MemberRepository;
import com.example.scenchive.domain.notification.dto.NotificationDto;
import com.example.scenchive.domain.notification.dto.ResponseNotificationDto;
import com.example.scenchive.domain.notification.repository.Notification;
import com.example.scenchive.domain.notification.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class NotificationService {
    private final NotificationRepository notificationRepository;
    private final MemberRepository memberRepository;

    @Autowired
    public NotificationService(NotificationRepository notificationRepository, MemberRepository memberRepository) {
        this.notificationRepository = notificationRepository;
        this.memberRepository = memberRepository;
    }

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    //알림 생성 메소드
    public void createNotification(Member member, String message, LocalDateTime created_at){
        Notification notification=Notification.builder()
                .member(member)
                .message(message)
                .memberCheck(false)
                .createdAt(created_at)
                .build();

        notificationRepository.save(notification);
    }

    //알림 클릭 시 메소드
    public void readNotification(Long notificationId){
        Notification notification = notificationRepository.findById(notificationId).get();
        if (!notification.isMemberCheck()){
            notification.checkNotification();
        }
    }


    //확인한 알림인지 알려주는 메소드
    public String checkBoolean(Long notificationId){
        Notification notification=notificationRepository.findById(notificationId).get();
        if(notification.isMemberCheck()){
            return "이미 확인한 알림입니다.";
        }
        else{
            return "확인하지 않은 알림입니다.";
        }
    }

    //알림 조회 메소드
    public ResponseNotificationDto getNotifications(Member member) {
        List<Notification> notifications = notificationRepository.findByMemberOrderByCreatedAtDesc(member);
        System.out.println("notifications = " + notifications);
        List<NotificationDto> notificationDtoList= notifications.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
        int readNotification=0;
        int unreadNotifications=0;
        for(NotificationDto notificationDto:notificationDtoList){
            if (notificationDto.isCheck()){
                readNotification+=1;
            }
            else{
                unreadNotifications+=1;
            }
        }
        ResponseNotificationDto responseNotificationDto=new ResponseNotificationDto(readNotification, unreadNotifications, notificationDtoList);
        return responseNotificationDto;
    }

    //notification 엔티티 -> notification Dto
    private NotificationDto mapToDto(Notification notification) {
        NotificationDto dto = new NotificationDto();
        dto.setId(notification.getId());
        dto.setMessage(notification.getMessage());
        LocalDateTime created_at=notification.getCreatedAt();
        String replyDate=created_at.format(formatter);
        dto.setCreatedAt(replyDate);
        dto.setCheck(notification.isMemberCheck());
        return dto;
    }

    //일주일 지난 알림 삭제
    @Scheduled(cron = "0 0 0 * * *") // 매일 자정에 실행
    public void deleteOldNotifications() {
        LocalDateTime oneWeekAgo = LocalDateTime.now().minusWeeks(1);
        notificationRepository.deleteByCreatedAtBefore(oneWeekAgo);
    }
}
