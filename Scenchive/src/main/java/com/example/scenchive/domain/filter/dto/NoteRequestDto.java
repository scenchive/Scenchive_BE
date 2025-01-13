package com.example.scenchive.domain.filter.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class NoteRequestDto {
    private List<String> topNote;
    private List<String> middleNote;
    private List<String> baseNote;

    @Builder
    public NoteRequestDto(List<String> topNote, List<String> middleNote, List<String> baseNote) {
        this.topNote = topNote;
        this.middleNote = middleNote;
        this.baseNote = baseNote;
    }
}
