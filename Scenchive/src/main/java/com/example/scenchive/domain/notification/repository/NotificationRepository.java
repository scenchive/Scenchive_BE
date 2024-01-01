package com.example.scenchive.domain.notification.repository;

import com.example.scenchive.domain.member.repository.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByMemberOrderByCreatedAtDesc(Member user);
    void deleteByCreatedAtBefore(LocalDateTime date);
}
