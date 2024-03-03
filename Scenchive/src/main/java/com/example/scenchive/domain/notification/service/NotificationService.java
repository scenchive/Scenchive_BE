package com.example.scenchive.domain.notification.service;

import com.example.scenchive.domain.board.repository.Board;
import com.example.scenchive.domain.member.repository.Member;
import com.example.scenchive.domain.member.repository.MemberRepository;
import com.example.scenchive.domain.notification.dto.NotificationDto;
import com.example.scenchive.domain.notification.dto.ResponseNotificationDto;
import com.example.scenchive.domain.notification.repository.Notification;
import com.example.scenchive.domain.notification.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
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
    public void createNotification(Member member, Board board, String message, LocalDateTime created_at){
        Notification notification=Notification.builder()
                .member(member)
                .board(board)
                .message(message)
                .memberCheck(false)
                .createdAt(created_at)
                .build();

        notificationRepository.save(notification);
    }

    //알림 클릭 시 메소드
    public void readNotification(Long notificationId){
        Notification notification = notificationRepository.findById(notificationId).orElseThrow(() -> new IllegalArgumentException("해당 알림이 없습니다."));
        if (!notification.isMemberCheck()){
            notification.checkNotification();
        }
    }


    //확인한 알림인지 알려주는 메소드
    public String checkBoolean(Long notificationId){
        Notification notification=notificationRepository.findById(notificationId).orElseThrow(() -> new IllegalArgumentException("해당 알림이 없습니다."));
        if(notification.isMemberCheck()){
            return "이미 확인한 알림입니다.";
        }
        else{
            return "확인하지 않은 알림입니다.";
        }
    }

    //알림 조회 메소드
    public ResponseNotificationDto getNotifications(Pageable pageable, Member member) {
        List<Notification> notifications = notificationRepository.findByMemberOrderByCreatedAtDesc(member);

        List<NotificationDto> notificationDtoList= notifications.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());

        List<NotificationDto> pagingNotifications=new ArrayList<>();

        int startIndex = (int) pageable.getOffset();
        int endIndex = Math.min(startIndex + pageable.getPageSize(), notificationDtoList.size());

        List<NotificationDto> paginatedNotifications = new ArrayList<>(notificationDtoList).subList(startIndex, endIndex);

        for(NotificationDto notification : paginatedNotifications){NotificationDto
                notificationDto=new NotificationDto(notification.getId(), notification.getBoardId(), notification.getBoardTitle(), notification.getMessage(), notification.getCreatedAt(), notification.isCheck());
            pagingNotifications.add(notificationDto);
        }

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
        ResponseNotificationDto responseNotificationDto=new ResponseNotificationDto(readNotification, unreadNotifications, pagingNotifications);
        return responseNotificationDto;
    }



    //notification 엔티티 -> notification Dto
    private NotificationDto mapToDto(Notification notification) {
        NotificationDto dto = new NotificationDto();
        dto.setId(notification.getId());
        dto.setBoardId(notification.getBoard().getId());
        dto.setBoardTitle(notification.getBoard().getTitle());
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
