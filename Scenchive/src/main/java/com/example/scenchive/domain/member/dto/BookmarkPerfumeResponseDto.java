package com.example.scenchive.domain.member.dto;

import com.example.scenchive.domain.filter.dto.SearchPerfumeDto;
import lombok.Getter;

import java.util.List;

@Getter
public class BookmarkPerfumeResponseDto {
    private long totalBookmarkPerfumeCount;
    private List<BookmarkPerfumeDto> perfumes;

    public BookmarkPerfumeResponseDto(long totalBookmarkPerfumeCount, List<BookmarkPerfumeDto> perfumes) {
        this.totalBookmarkPerfumeCount = totalBookmarkPerfumeCount;
        this.perfumes = perfumes;
    }
}
