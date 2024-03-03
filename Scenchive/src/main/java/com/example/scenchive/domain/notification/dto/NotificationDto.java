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

    public NotificationDto(Long id, Long boardId, String boardTitle, String message, String createdAt, boolean check) {
        this.id = id;
        this.boardId = boardId;
        this.boardTitle = boardTitle;
        this.message = message;
        this.createdAt = createdAt;
        this.check = check;
    }

    public NotificationDto() {
    }
}
