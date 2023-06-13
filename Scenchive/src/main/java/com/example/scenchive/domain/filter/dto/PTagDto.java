package com.example.scenchive.domain.filter.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PTagDto {
    private Long id;

    private String ptag_name;

    private String ptag_kr;

    private int ptagtype_id;

    public PTagDto(Long id, String ptag_name, String ptag_kr, int ptagtype_id) {
        this.id = id;
        this.ptag_name = ptag_name;
        this.ptag_kr = ptag_kr;
        this.ptagtype_id = ptagtype_id;
    }
}
