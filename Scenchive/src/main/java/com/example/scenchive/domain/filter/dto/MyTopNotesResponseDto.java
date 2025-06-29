package com.example.scenchive.domain.filter.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class MyTopNotesResponseDto {
    private List<NoteWithCountDto> topNote;
    private List<NoteWithCountDto> middleNote;
    private List<NoteWithCountDto> baseNote;
}
