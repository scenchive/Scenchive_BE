package com.example.scenchive.domain.filter.service;

import com.example.scenchive.domain.filter.dto.PTagDto;
import com.example.scenchive.domain.filter.dto.PerfumeDto;
import com.example.scenchive.domain.filter.repository.*;
import com.example.scenchive.domain.info.repository.PerfumenoteRepository;
import com.example.scenchive.domain.info.repository.PerfumescentRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Transactional
public class PerfumeService {
    private final PerfumeTagRepository perfumeTagRepository;
    private final PerfumeRepository perfumeRepository;
    private final BrandRepository brandRepository;
    private final PTagRepository pTagRepository;
    private PerfumescentRepository perfumescentRepository;
    private PerfumenoteRepository perfumenoteRepository;

    @Autowired
    public PerfumeService(PerfumeTagRepository perfumeTagRepository, PerfumeRepository perfumeRepository,
                          BrandRepository brandRepository, PTagRepository pTagRepository,
                          PerfumescentRepository perfumescentRepository, PerfumenoteRepository perfumenoteRepository) {

        this.perfumeTagRepository = perfumeTagRepository;
        this.perfumeRepository = perfumeRepository;
        this.brandRepository = brandRepository;
        this.pTagRepository = pTagRepository;
        this.perfumescentRepository = perfumescentRepository;
        this.perfumenoteRepository = perfumenoteRepository;
    }

    @Transactional(readOnly = true)
    // 키워드 필터링 결과로 나온 향수 리스트 조회
    public List<PerfumeDto> getPerfumesByKeyword(List<PTag> keywordIds, Pageable pageable) {
        // 주어진 키워드 id들로 PerfumeTag 리스트 조회
        List<PerfumeTag> perfumeTags = perfumeTagRepository.findByPtagIn(keywordIds);
        // perfume_id 오름차순으로 조회
        Set<Perfume> uniquePerfumes = new TreeSet<>((p1, p2) -> p1.getId().compareTo(p2.getId())); // Set: 다중 키워드로 인해 중복된 향수가 있는 경우 제거

        for (PerfumeTag perfumeTag : perfumeTags) {
            Perfume perfume = perfumeTag.getPerfume();
            uniquePerfumes.add(perfume);
        }

        List<PerfumeDto> perfumes = new ArrayList<>();

        int startIndex = (int) pageable.getOffset();
        int endIndex = Math.min(startIndex + pageable.getPageSize(), uniquePerfumes.size());

        List<Perfume> paginatedPerfumes = new ArrayList<>(uniquePerfumes).subList(startIndex, endIndex);

        for (Perfume perfume : paginatedPerfumes) {
            List<Long> perfumeKeywordIds = new ArrayList<>();
            for (PerfumeTag perfumeTag : perfumeTags) {
                if (perfumeTag.getPerfume().equals(perfume)) {
                    perfumeKeywordIds.add(perfumeTag.getPtag().getId());
                }
            }

            String cleanedFileName = perfume.getPerfumeName().replaceAll("[^\\w]", "");
            String perfumeImage = "https://scenchive.s3.ap-northeast-2.amazonaws.com/perfume/" + cleanedFileName + ".jpg";
            Brand brand = brandRepository.findById(perfume.getBrandId()).orElse(null);
            String brandName = (brand != null) ? brand.getBrandName() : null;
            String brandName_kr = (brand != null) ? brand.getBrandName_kr() : null;
            PerfumeDto perfumeDto = new PerfumeDto(perfume.getId(), perfume.getPerfumeName(), perfumeImage, brandName, brandName_kr, perfumeKeywordIds);
            perfumes.add(perfumeDto);
        }

        // 향수 DTO 리스트 반환
        return perfumes;
    }

    @Transactional(readOnly = true)
    // 키워드 필터링 결과로 나온 전체 향수 개수 구하기
    public int getTotalPerfumeCount(List<PTag> keywordIds) {
        List<PerfumeTag> perfumeTags = perfumeTagRepository.findByPtagIn(keywordIds);
        Set<Perfume> uniquePerfumes = new HashSet<>();

        for (PerfumeTag perfumeTag : perfumeTags) {
            Perfume perfume = perfumeTag.getPerfume();
            uniquePerfumes.add(perfume);
        }
        return uniquePerfumes.size();
    }

    //필터 추천 키워드 조회
    public List<PTagDto> getTypeKeyword(){ //계열, 분위기, 계절 키워드
        List<PTagDto> pTagDtos=new ArrayList<>();
        for (long i=1;i<=25;i++){
            PTagDto pTagDto=new PTagDto(i,
                    pTagRepository.findById(i).get().getPtagName(),
                    pTagRepository.findById(i).get().getPtagKr(),
                    pTagRepository.findById(i).get().getPtagType().getId());
            pTagDtos.add(pTagDto);
        }

        for (long i=36;i<=39;i++){
            PTagDto pTagDto=new PTagDto(i,
                    pTagRepository.findById(i).get().getPtagName(),
                    pTagRepository.findById(i).get().getPtagKr(),
                    pTagRepository.findById(i).get().getPtagType().getId());
            pTagDtos.add(pTagDto);
        }
        return pTagDtos;
    }

    public List<PTagDto> getTPOKeyword(){ //장소, 분위기, 계열 키워드
        List<PTagDto> pTagDtos=new ArrayList<>();
        for (long i=1;i<=35;i++){
            PTagDto pTagDto=new PTagDto(i,
                    pTagRepository.findById(i).get().getPtagName(),
                    pTagRepository.findById(i).get().getPtagKr(),
                    pTagRepository.findById(i).get().getPtagType().getId());
            pTagDtos.add(pTagDto);
        }
        return pTagDtos;
    }
}
