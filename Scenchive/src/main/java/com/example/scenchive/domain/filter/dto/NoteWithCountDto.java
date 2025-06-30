package com.example.scenchive.domain.filter.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class NoteWithCountDto {
    private String scent;
    private String scentKr;
    private int count;
}
