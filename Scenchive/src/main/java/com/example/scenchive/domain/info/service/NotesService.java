package com.example.scenchive.domain.info.service;

import com.example.scenchive.domain.filter.repository.Perfume;
import com.example.scenchive.domain.filter.repository.PerfumeRepository;
import com.example.scenchive.domain.info.dto.NotesInfoResponse;
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

    @Autowired
    public NotesService(PerfumescentRepository perfumescentRepository, PerfumenoteRepository perfumenoteRepository, PerfumeRepository perfumeRepository) {
        this.perfumescentRepository = perfumescentRepository;
        this.perfumenoteRepository=perfumenoteRepository;
        this.perfumeRepository=perfumeRepository;
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
}
