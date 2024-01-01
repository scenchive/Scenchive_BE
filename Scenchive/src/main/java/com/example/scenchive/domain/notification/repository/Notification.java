package com.example.scenchive.domain.notification.repository;

import com.example.scenchive.domain.BaseTimeEntity;
import com.example.scenchive.domain.member.repository.Member;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@Entity
@Table(name="notification")
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private Member member;

    @Column(nullable = false)
    private String message;

    @Column(nullable = false)
    private boolean memberCheck;

    private LocalDateTime createdAt;

    public void checkNotification(){
        this.memberCheck=true;
    }

    @Builder
    public Notification(Member member, String message, boolean memberCheck, LocalDateTime createdAt) {
        this.member = member;
        this.message = message;
        this.memberCheck = memberCheck;
        this.createdAt = createdAt;
    }
}