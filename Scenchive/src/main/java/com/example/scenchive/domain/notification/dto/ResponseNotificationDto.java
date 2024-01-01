package com.example.scenchive.domain.notification.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class ResponseNotificationDto {
    private int readNotifications;
    private int unreadNotifications;
    private List<NotificationDto> notificationDtoList;

    public ResponseNotificationDto(int readNotifications, int unreadNotifications, List<NotificationDto> notificationDtoList) {
        this.readNotifications = readNotifications;
        this.unreadNotifications = unreadNotifications;
        this.notificationDtoList = notificationDtoList;
    }
}
