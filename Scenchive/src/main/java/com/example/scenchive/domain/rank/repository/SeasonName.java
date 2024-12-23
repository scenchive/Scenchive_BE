package com.example.scenchive.domain.rank.repository;

import lombok.Getter;

@Getter
public enum SeasonName {

    SPRING(3, 5),
    SUMMER(6, 8),
    AUTUMN(9, 11),
    WINTER(12, 2);

    private final int startMonth;
    private final int endMonth;

    SeasonName(int startMonth, int endMonth) {
        this.startMonth = startMonth;
        this.endMonth = endMonth;
    }

}
