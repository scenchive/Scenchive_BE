package com.example.scenchive.domain.member.repository;

import com.example.scenchive.domain.board.repository.Board;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="user")
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;

    private String name;

    private String password;

    @ManyToMany
    @JoinTable(
            name = "user_authority",
            joinColumns = {@JoinColumn(name = "id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "authority_name", referencedColumnName = "authority_name")})
    private Set<Authority> authorities;
    @Column(name = "activated")
    private Boolean activated;

    public Boolean isActivated() {
        return activated != null ? activated : false;
    }

    public void setActivated(Boolean activated) {
        this.activated = activated;

    @OneToMany(mappedBy = "member")
    @Builder.Default
    private List<Board> boards=new ArrayList<>();
    //다 쪽에서 FK를 가지고 일 쪽에서 mappedby, list객체 가짐

    @OneToMany(mappedBy="member")
    @Builder.Default
    private List<UserTag> userTags=new ArrayList<>();

    @OneToMany(mappedBy = "member")
    @Builder.Default
    private List<perfumeMarked> perfumeMarkedList=new ArrayList<>();

//    @OneToMany(mappedBy = "member", cascade = ALL, orphanRemoval = true)
//    private List<Comment> commentList = new ArrayList<>();

//    public void addComment(Comment comment) {
//        commentList.add(comment);
//    }

//    @Builder
//    public Member(String email, String name, String password) {
//        this.email = email;
//        this.name = name;
//        this.password = password;
//    }

    public Member(String name) {
        this.name = name;
    }

    public Member(Long id) {
        this.id = id;
    }

    public boolean checkPassword(String password){
        return this.password.equals(password);
    }
}