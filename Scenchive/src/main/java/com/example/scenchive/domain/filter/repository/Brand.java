package com.example.scenchive.domain.filter.repository;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "brand" )
public class Brand {
    public Brand(String brandName, String brandName_kr, String brandUrl) {
        this.brandName = brandName;
        this.brandName_kr = brandName_kr;
        this.brandUrl = brandUrl;
    }

    public Brand(String brandName, String brandName_kr) {
        this.brandName = brandName;
        this.brandName_kr = brandName_kr;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "brand_name")
    private String brandName;

    @Column(name="brand_kr")
    private String brandName_kr;

    @Column(name="brand_url")
    private String brandUrl;
}
