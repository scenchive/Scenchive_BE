package com.example.scenchive.domain.filter.service;

import com.example.scenchive.domain.filter.dto.PerfumeDto;
import com.example.scenchive.domain.filter.repository.Perfume;
import com.example.scenchive.domain.filter.repository.PerfumeRepository;
import com.example.scenchive.domain.filter.repository.PerfumeTag;
import com.example.scenchive.domain.filter.repository.PerfumeTagRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class PerfumeService {
    private final PerfumeTagRepository perfumeTagRepository;
    private final PerfumeRepository perfumeRepository;

    @Autowired
    public PerfumeService(PerfumeTagRepository perfumeTagRepository, PerfumeRepository perfumeRepository) {

        this.perfumeTagRepository = perfumeTagRepository;
        this.perfumeRepository = perfumeRepository;
    }

    public List<PerfumeDto> getPerfumesByKeyword(Long keywordId) {
        // 주어진 키워드 id들로 PerfumeTag 리스트 조회
        List<PerfumeTag> perfumeTags = perfumeTagRepository.findByPtagId(keywordId);
        List<PerfumeDto> perfumes = new ArrayList<>();

        for (PerfumeTag perfumeTag : perfumeTags) {
            Perfume perfume = perfumeTag.getPerfume();
            PerfumeDto perfumeDto = new PerfumeDto(perfume.getId(), perfume.getPerfumeName());
            perfumes.add(perfumeDto);
        }

        // 향수 DTO 리스트 반환
        return perfumes;
    }
}
