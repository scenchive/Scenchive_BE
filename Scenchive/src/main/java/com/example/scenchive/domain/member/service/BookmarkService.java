package com.example.scenchive.domain.member.service;

import com.example.scenchive.domain.filter.repository.Perfume;
import com.example.scenchive.domain.filter.repository.PerfumeRepository;
import com.example.scenchive.domain.member.dto.perfumeMarkedDto;
import com.example.scenchive.domain.member.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
public class BookmarkService {

    private final perfumeMarkedRepository perfumemarkedRepository;
    private final MemberRepository memberRepository;
    private final PerfumeRepository perfumeRepository;


    @Autowired
    public BookmarkService(perfumeMarkedRepository perfumemarkedRepository, MemberRepository memberRepository, PerfumeRepository perfumeRepository){
        this.perfumemarkedRepository=perfumemarkedRepository;
        this.memberRepository=memberRepository;
        this.perfumeRepository=perfumeRepository;
    }

    @Transactional
    public perfumeMarkedDto bookmarkSave(Long userId, Long perfumeId){
        Member member=memberRepository.findById(userId).get();
        Perfume perfume=perfumeRepository.findById(perfumeId).get();

        perfumeMarked perfumemarked=perfumeMarked.builder()
                .member(member)
                .perfume(perfume)
                .build();

        perfumemarkedRepository.save(perfumemarked);

        perfumeMarkedDto perfumemarkedDto=new perfumeMarkedDto(userId, perfumeId);
        return perfumemarkedDto;
    }

    @Transactional
    public void bookmarkDelete(Long userId, Long perfumeId){
        Member member=memberRepository.findById(userId).get();
        Perfume perfume=perfumeRepository.findById(perfumeId).get();
        perfumeMarked perfumemarked=perfumemarkedRepository.findByMemberAndPerfume(member, perfume).get();
        perfumemarkedRepository.delete(perfumemarked);
    }
}
