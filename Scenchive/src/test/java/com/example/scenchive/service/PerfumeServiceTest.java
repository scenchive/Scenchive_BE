package com.example.scenchive.service;

import com.example.scenchive.domain.filter.dto.PerfumeDto;
import com.example.scenchive.domain.filter.repository.PerfumeTag;
import com.example.scenchive.domain.filter.repository.PerfumeTagRepository;
import com.example.scenchive.domain.filter.service.PerfumeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class PerfumeServiceTest {
    @Mock
    private PerfumeTagRepository perfumeTagRepository;

    private PerfumeService perfumeService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        perfumeService = new PerfumeService(perfumeTagRepository);
    }

    @Test
    public void testGetPerfumesByKeywords() {
        // 테스트에 필요한 가상의 데이터 생성
        PerfumeTag perfumeTag1 = new PerfumeTag();
        PerfumeTag perfumeTag2 = new PerfumeTag();
        PerfumeTag perfumeTag3 = new PerfumeTag();

        List<PerfumeTag> perfumeTags = Arrays.asList(perfumeTag1, perfumeTag2, perfumeTag3);
        List<Long> keywordIds = Arrays.asList(1L, 2L);

        // 가상의 데이터를 반환하도록 mock 설정
        when(perfumeTagRepository.findByPtagId(keywordIds)).thenReturn(perfumeTags);

        // 테스트 대상 메서드 호출
        List<PerfumeDto> result = perfumeService.getPerfumesByKeyword(keywordIds);

        // 예상 결과와 실제 결과 비교
        List<PerfumeDto> expected = new ArrayList<>();
        expected.add(new PerfumeDto(1L, "Perfume A"));
        expected.add(new PerfumeDto(2L, "Perfume B"));

        assertEquals(expected, result);
    }
}
