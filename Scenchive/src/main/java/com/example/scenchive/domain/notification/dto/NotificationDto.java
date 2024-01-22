package com.example.scenchive.domain.notification.dto;

import com.example.scenchive.domain.notification.repository.Notification;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NotificationDto {
    private Long id;
    private Long boardId;
    private String boardTitle;
    private String message;
    private String createdAt;
    private boolean check;

}
