package com.example.scenchive.domain.review.repository;

import com.example.scenchive.domain.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "review")
public class Review extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id")
    private Long memberId;

    @Column(name = "perfume_id")
    private Long perfumeId;

    @Column(name = "rating")
    private int rating;

    @Column(name = "longevity")
    private int longevity;

    @Column(name = "sillage")
    private int sillage;

    @Column(name = "content", length = 1000)
    private String content;

    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt;
}
