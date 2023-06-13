package com.example.scenchive.domain.review.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class PerfumeRatingDto {
    private Long perfumeId;
    private double ratingAvg;
    private double longevityAvg;
    private double sillageAvg;
    private Map<String, Double> seasonAvg;
}
