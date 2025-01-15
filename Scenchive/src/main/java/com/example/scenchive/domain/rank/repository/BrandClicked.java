package com.example.scenchive.domain.rank.repository;

import com.example.scenchive.domain.filter.repository.Brand;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "brandclicked")
public class BrandClicked {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "brand_id")
    private Brand brand;

    private int clickCount;

    public BrandClicked(Brand brand) {
        this.brand = brand;
        clickCount = 1;
    }
}
