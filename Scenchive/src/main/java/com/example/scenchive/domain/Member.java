package com.example.scenchive.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
@Table(name="user")
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;

    private String name;

    private int password;

    @Builder
    public Member(String email, String name, int password) {
        this.email = email;
        this.name = name;
        this.password = password;
    }

    public boolean checkPassword(int password){
        return this.password==password;
    }
}
