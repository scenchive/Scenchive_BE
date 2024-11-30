package com.example.scenchive.domain.filter.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class PerfumeCollectedRequestDto {
    private Long memberId;
    private Long perfumeId;
}
