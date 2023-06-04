package com.example.scenchive.domain.filter.repository;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "ptag")
public class PTag {
    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name = "id")
    private Long id;

    @JoinColumn(name = "ptag_name")
    private String ptagName;

    @JoinColumn(name = "ptag_kr")
    private String ptagKr;

}
