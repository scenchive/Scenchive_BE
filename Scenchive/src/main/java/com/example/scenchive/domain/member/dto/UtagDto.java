package com.example.scenchive.domain.member.dto;

import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UtagDto {
    private Long id;

    private String utag;

    private String utag_kr;

    private int utagtype_id;

    public UtagDto(Long id, String utag, String utag_kr, int utagtype_id) {
        this.id = id;
        this.utag = utag;
        this.utag_kr = utag_kr;
        this.utagtype_id = utagtype_id;
    }
}
