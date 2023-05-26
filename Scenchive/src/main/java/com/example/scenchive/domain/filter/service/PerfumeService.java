package com.example.scenchive.domain.filter.service;

import com.example.scenchive.domain.filter.dto.PerfumeDto;
import com.example.scenchive.domain.filter.repository.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Transactional
public class PerfumeService {
    private final PerfumeTagRepository perfumeTagRepository;
    private final PerfumeRepository perfumeRepository;
    private final BrandRepository brandRepository;

    @Autowired
    public PerfumeService(PerfumeTagRepository perfumeTagRepository, PerfumeRepository perfumeRepository, BrandRepository brandRepository) {

        this.perfumeTagRepository = perfumeTagRepository;
        this.perfumeRepository = perfumeRepository;
        this.brandRepository = brandRepository;
    }

    public List<PerfumeDto> getPerfumesByKeyword(List<PTag> keywordIds) {
        // 주어진 키워드 id들로 PerfumeTag 리스트 조회
        List<PerfumeTag> perfumeTags = perfumeTagRepository.findByPtagIn(keywordIds);
        // perfume_id 오름차순으로 조회
        Set<Perfume> uniquePerfumes = new TreeSet<>((p1, p2) -> p1.getId().compareTo(p2.getId())); // Set: 다중 키워드로 인해 중복된 향수가 있는 경우 제거

        for (PerfumeTag perfumeTag : perfumeTags) {
            Perfume perfume = perfumeTag.getPerfume();
            uniquePerfumes.add(perfume);
        }

        List<PerfumeDto> perfumes = new ArrayList<>();

        for (Perfume perfume : uniquePerfumes) {
            List<Long> perfumeKeywordIds = new ArrayList<>();
            for (PerfumeTag perfumeTag : perfumeTags) {
                if (perfumeTag.getPerfume().equals(perfume)) {
                    perfumeKeywordIds.add(perfumeTag.getPtag().getId());
                }
            }

            Brand brand = brandRepository.findById(perfume.getBrandId()).orElse(null);
            String brandName = (brand != null) ? brand.getBrandName() : null;
            PerfumeDto perfumeDto = new PerfumeDto(perfume.getId(), perfume.getPerfumeName(), brandName, perfumeKeywordIds);
            perfumes.add(perfumeDto);
        }

        // 향수 DTO 리스트 반환
        return perfumes;
    }
}
