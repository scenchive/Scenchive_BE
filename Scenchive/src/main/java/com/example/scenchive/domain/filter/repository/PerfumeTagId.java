package com.example.scenchive.domain.filter.repository;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class PerfumeTagId implements Serializable {
    private Perfume perfume;
    private PTag ptag;
}
