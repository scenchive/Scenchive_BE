package com.example.scenchive.domain.info.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class NotesInfoDto {
    private List<String> top;
    private List<String> middle;
    private List<String> base;

    public NotesInfoDto(List<String> top, List<String> middle, List<String> base) {
        this.top = top;
        this.middle = middle;
        this.base = base;
    }
}
