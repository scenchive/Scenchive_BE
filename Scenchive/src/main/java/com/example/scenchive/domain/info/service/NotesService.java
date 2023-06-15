package com.example.scenchive.domain.info.service;

import com.example.scenchive.domain.info.dto.NotesInfoResponse;
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

    @Autowired
    public NotesService(PerfumescentRepository perfumescentRepository) {
        this.perfumescentRepository = perfumescentRepository;
    }

    // 탑노트 반환
    public List<String> getTopNotesByPerfumeId(Long perfumeId) {
        List<Perfumescent> perfumeScents = perfumescentRepository.findByPerfumeIdAndNoteId(perfumeId, 1L);
        System.out.println("top notes:" + perfumeScents);
        List<String> topNotes = perfumeScents.stream()
                .map(Perfumescent::getScentKr)
                .collect(Collectors.toList());
        return topNotes;
    }

    // 미들노트 반환
    public List<String> getMiddleNotesByPerfumeId(Long perfumeId) {
        List<Perfumescent> perfumeScents = perfumescentRepository.findByPerfumeIdAndNoteId(perfumeId, 2L);
        System.out.println("middle notes:" + perfumeScents);
        List<String> middleNotes = perfumeScents.stream()
                .map(Perfumescent::getScentKr)
                .collect(Collectors.toList());
        return middleNotes;
    }

    // 베이스노트 반환
    public List<String> getBaseNotesByPerfumeId(Long perfumeId) {
            List<Perfumescent> perfumeScents = perfumescentRepository.findByPerfumeIdAndNoteId(perfumeId, 3L);
            System.out.println("base notes:" + perfumeScents);
            List<String> baseNotes = perfumeScents.stream()
                    .map(Perfumescent::getScentKr)
                    .collect(Collectors.toList());
            return baseNotes;
    }
}
