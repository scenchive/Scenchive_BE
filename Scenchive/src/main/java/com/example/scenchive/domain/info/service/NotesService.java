package com.example.scenchive.domain.info.service;

import com.example.scenchive.domain.filter.repository.Brand;
import com.example.scenchive.domain.filter.repository.BrandRepository;
import com.example.scenchive.domain.filter.repository.Perfume;
import com.example.scenchive.domain.filter.repository.PerfumeRepository;
import com.example.scenchive.domain.info.dto.NotesInfoResponse;
import com.example.scenchive.domain.info.dto.PerfumescentDto;
import com.example.scenchive.domain.info.repository.Perfumenote;
import com.example.scenchive.domain.info.repository.PerfumenoteRepository;
import com.example.scenchive.domain.info.repository.Perfumescent;
import com.example.scenchive.domain.info.repository.PerfumescentRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class NotesService {
    private final PerfumescentRepository perfumescentRepository;
    private final PerfumenoteRepository perfumenoteRepository;
    private final PerfumeRepository perfumeRepository;
    private final BrandRepository brandRepository;

    @Autowired
    public NotesService(PerfumescentRepository perfumescentRepository, PerfumenoteRepository perfumenoteRepository, PerfumeRepository perfumeRepository, BrandRepository brandRepository) {
        this.perfumescentRepository = perfumescentRepository;
        this.perfumenoteRepository=perfumenoteRepository;
        this.perfumeRepository=perfumeRepository;
        this.brandRepository = brandRepository;
    }

    // 향수 이름
    public String getPerfumeNameByPerfumeId(Long perfumeId) {
        Perfume perfume = perfumeRepository.findById(perfumeId).get();
        String perfumeName = perfume.getPerfumeName();
        return perfumeName;
    }

    public String getBrandNameByPerfumeId(Long perfumeId) {
        Perfume perfume = perfumeRepository.findById(perfumeId).get();
        Long brandId = perfume.getBrandId();
        Brand brand = brandRepository.findById(brandId).get();
        String brandName = brand.getBrandName();
        return brandName;
    }

    // 노트 생성
    public PerfumescentDto createNotes(PerfumescentDto perfumescentDto){
        Perfume perfume = perfumeRepository.findById(perfumescentDto.getPerfumeId()).orElse(null);

        List<List<String>> scents = perfumescentDto.getScents();
        for (int i = 0; i < scents.size(); i++) {
            List<String> scent = scents.get(i);

            // entity
            Perfumenote perfumenote = perfumenoteRepository.findById(Long.valueOf(scent.get(2))).orElse(null);
            Perfumescent perfumescent = new Perfumescent(perfume, perfumenote, scent.get(0), scent.get(1));

            // entity save
            perfumescentRepository.save(perfumescent);
        }

        return perfumescentDto;
    }

    // 탑노트 반환
    public List<String> getTopNotesByPerfumeId(Long perfumeId) {
        Perfume perfume=perfumeRepository.findById(perfumeId).get();
        Perfumenote perfumenote=perfumenoteRepository.findById(1L).get();
        List<Perfumescent> perfumeScents = perfumescentRepository.findByPerfumeAndPerfumenote(perfume, perfumenote);
//        System.out.println("top notes:" + perfumeScents);
        List<String> topNotes = perfumeScents.stream()
                .map(Perfumescent::getScentKr)
                .collect(Collectors.toList());
        return topNotes;
    }

    // 미들노트 반환
    public List<String> getMiddleNotesByPerfumeId(Long perfumeId) {
        Perfume perfume=perfumeRepository.findById(perfumeId).get();
        Perfumenote perfumenote=perfumenoteRepository.findById(2L).get();
        List<Perfumescent> perfumeScents = perfumescentRepository.findByPerfumeAndPerfumenote(perfume, perfumenote);
//        System.out.println("middle notes:" + perfumeScents);
        List<String> middleNotes = perfumeScents.stream()
                .map(Perfumescent::getScentKr)
                .collect(Collectors.toList());
        return middleNotes;
    }

    // 베이스노트 반환
    public List<String> getBaseNotesByPerfumeId(Long perfumeId) {
        Perfume perfume=perfumeRepository.findById(perfumeId).get();
        Perfumenote perfumenote=perfumenoteRepository.findById(3L).get();
        List<Perfumescent> perfumeScents = perfumescentRepository.findByPerfumeAndPerfumenote(perfume, perfumenote);
//            System.out.println("base notes:" + perfumeScents);
        List<String> baseNotes = perfumeScents.stream()
                .map(Perfumescent::getScentKr)
                .collect(Collectors.toList());
        return baseNotes;
    }

//    public PerfumescentDto mapToDto(Perfumescent perfumescent){
//        return new PerfumescentDto(
//                perfumescent.getPerfume().getId(),
//                perfumescent.getPerfumenote().getId(),
//                perfumescent.getScent(),
//                perfumescent.getScentKr()
//        );
//    }
}
