package com.example.scenchive.domain.member.service;

import com.example.scenchive.domain.filter.dto.SearchPerfumeDto;
import com.example.scenchive.domain.filter.repository.*;
import com.example.scenchive.domain.filter.utils.DeduplicationUtils;
import com.example.scenchive.domain.member.dto.BookmarkPerfumeDto;
import com.example.scenchive.domain.member.dto.BookmarkPerfumeResponseDto;
import com.example.scenchive.domain.member.dto.perfumeMarkedDto;
import com.example.scenchive.domain.member.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Transactional(readOnly = true)
@Service
public class BookmarkService {

    private final perfumeMarkedRepository perfumemarkedRepository;
    private final MemberRepository memberRepository;
    private final PerfumeRepository perfumeRepository;
    private final BrandRepository brandRepository;
    private final PerfumeTagRepository perfumeTagRepository;
    private final PTagRepository ptagRepository;


    @Autowired
    public BookmarkService(perfumeMarkedRepository perfumemarkedRepository, MemberRepository memberRepository, PerfumeRepository perfumeRepository,
                           BrandRepository brandRepository, PerfumeTagRepository perfumeTagRepository, PTagRepository ptagRepository){
        this.perfumemarkedRepository=perfumemarkedRepository;
        this.memberRepository=memberRepository;
        this.perfumeRepository=perfumeRepository;
        this.brandRepository=brandRepository;
        this.perfumeTagRepository=perfumeTagRepository;
        this.ptagRepository=ptagRepository;
    }

    //북마크한 향수인지 확인
    @Transactional
    public String checkMarked(Long userId, Long perfumeId){
        Member member=memberRepository.findById(userId).get();
        List<perfumeMarked> perfumeMarkedList = perfumemarkedRepository.findByMember(member);
        for (perfumeMarked perfumemarked : perfumeMarkedList){
            Perfume userPerfume=perfumemarked.getPerfume();
            if (userPerfume.getId().equals(perfumeId)){
                return "이미 북마크한 향수입니다.";
            }
        }
        return "북마크한 향수가 아닙니다.";
    }

    //북마크 저장
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

    //북마크 삭제
    @Transactional
    public String bookmarkDelete(Long userId, Long perfumeId){
        Member member=memberRepository.findById(userId).get();
        Perfume perfume=perfumeRepository.findById(perfumeId).get();
        perfumeMarked perfumemarked=perfumemarkedRepository.findByMemberAndPerfume(member, perfume).get();
        perfumemarkedRepository.delete(perfumemarked);
        return "북마크 해제되었습니다.";
    }

    //북마크한 향수 전체 개수 조회
    public long getTotalBookmarkPerfumeCount(Long userId, Pageable pageable){
        List<BookmarkPerfumeDto> bookmarkPerfumeDtos=new ArrayList<>();

        Member member=memberRepository.findById(userId).get();
        List<perfumeMarked> perfumeMarkedList=perfumemarkedRepository.findByMember(member);

        for (perfumeMarked perfumemarked : perfumeMarkedList){
            Perfume perfume=perfumemarked.getPerfume();
            Long perfume_id= perfume.getId();
            String perfume_name=perfume.getPerfumeName();
            String brand_name=brandRepository.findById(perfume.getBrandId()).get().getBrandName();
            BookmarkPerfumeDto bookmarkPerfumeDto=new BookmarkPerfumeDto(perfume_id, perfume_name, brand_name);
            bookmarkPerfumeDtos.add(bookmarkPerfumeDto);
        }
        return bookmarkPerfumeDtos.size();
    }


    //마이페이지 : 북마크한 향수 목록 조회
    public List<BookmarkPerfumeDto> getBookmarkPerfume(Long userId, Pageable pageable){
        List<BookmarkPerfumeDto> bookmarkPerfumeDtos=new ArrayList<>();

        Member member=memberRepository.findById(userId).get();
        List<perfumeMarked> perfumeMarkedList=perfumemarkedRepository.findByMember(member);

        for (perfumeMarked perfumemarked : perfumeMarkedList){
            Perfume perfume=perfumemarked.getPerfume();
            Long perfume_id= perfume.getId();
            String perfume_name=perfume.getPerfumeName();
            String brand_name=brandRepository.findById(perfume.getBrandId()).get().getBrandName();
            BookmarkPerfumeDto bookmarkPerfumeDto=new BookmarkPerfumeDto(perfume_id, perfume_name, brand_name);
            bookmarkPerfumeDtos.add(bookmarkPerfumeDto);
        }
//        return bookmarkPerfumeDtos;

        List<BookmarkPerfumeDto> perfumes=new ArrayList<>();

        int startIndex = (int) pageable.getOffset();
        int endIndex = Math.min(startIndex + pageable.getPageSize(), bookmarkPerfumeDtos.size());

        List<BookmarkPerfumeDto> paginatedPerfumes = new ArrayList<>(bookmarkPerfumeDtos).subList(startIndex, endIndex);

        for(BookmarkPerfumeDto perfume : paginatedPerfumes){
            BookmarkPerfumeDto bookmarkPerfumeDto=new BookmarkPerfumeDto(perfume.getPerfume_id(), perfume.getPerfume_name(), perfume.getBrand_name());
            perfumes.add(bookmarkPerfumeDto);
        }
        return perfumes;
    }

    //마이페이지 : 북마크한 향수와 유사한 향수 목록 조회
    public List<BookmarkPerfumeDto> getSimilarPerfume(Long userId, Pageable pageable){
        List<BookmarkPerfumeDto> similarPerfumeDtos=new ArrayList<>();
        List<Long> ptagIds=new ArrayList<>();
        List<Long> bookmarkPerfumeIds=new ArrayList<>();

        Member member=memberRepository.findById(userId).get();
        List<perfumeMarked> perfumeMarkedList=perfumemarkedRepository.findByMember(member);

        for (perfumeMarked perfumemarked : perfumeMarkedList){ //유저가 북마크한 향수가 여러개이므로 향수 하나씩 돌기
            Perfume perfume=perfumemarked.getPerfume();
            Long perfume_id=perfume.getId();

            bookmarkPerfumeIds.add(perfume_id); // 유저가 북마크한 향수 아이디 저장해두기

            List<PerfumeTag> perfumeTags=perfumeTagRepository.findByPerfume(perfume); //향수별로 태그가 여러개이므로 태그 리스트로 받기
            for (PerfumeTag perfumeTag : perfumeTags){ //태그 하나씩 돌면서 ptagid 저장하기
                Long ptag_id=perfumeTag.getPtag().getId();
                if(ptagIds.contains(ptag_id)==false){ //ptag_id 중복 저장 방지
                    ptagIds.add(ptag_id);
                }
            }
        }

       for (Long id : ptagIds){ //ptag 하나씩 돌기
            PTag ptag=ptagRepository.findById(id).get();
            List<PerfumeTag> perfumeTagList=perfumeTagRepository.findByPtag(ptag); // 해당 ptag를 가진 향수태그리스트 생성
            for (PerfumeTag perfumeTag : perfumeTagList){ //향수태그 하나씩 돌면서 향수 찾기
                Long perfumeId=perfumeTag.getPerfumeId();

                if(bookmarkPerfumeIds.contains(perfumeId)==false){ //사용자가 북마크한 향수 목록에 없는 향수인 경우(북마크한 향수 추천 방지)
                    Perfume perfume=perfumeRepository.findById(perfumeId).get();

                    Long perfume_id=perfume.getId();
                    String perfume_name=perfume.getPerfumeName();
                    String brand_name=brandRepository.findById(perfume.getBrandId()).get().getBrandName();
                    BookmarkPerfumeDto bookmarkPerfumeDto=new BookmarkPerfumeDto(perfume_id, perfume_name, brand_name);

                    if(similarPerfumeDtos.contains(bookmarkPerfumeDto)==false){ // 향수 추천 목록에 안 들어간 향수인 경우(중복 저장 방지)
                        similarPerfumeDtos.add(bookmarkPerfumeDto);
                    }
                }
            }
        }

       similarPerfumeDtos= DeduplicationUtils.deduplication(similarPerfumeDtos, BookmarkPerfumeDto::getPerfume_name);
//        return similarPerfumeDtos;

        List<BookmarkPerfumeDto> perfumes=new ArrayList<>();

        int startIndex = (int) pageable.getOffset();
        int endIndex = Math.min(startIndex + pageable.getPageSize(), similarPerfumeDtos.size());

        List<BookmarkPerfumeDto> paginatedPerfumes = new ArrayList<>(similarPerfumeDtos).subList(startIndex, endIndex);

        for(BookmarkPerfumeDto perfume : paginatedPerfumes){
            BookmarkPerfumeDto bookmarkPerfumeDto = new BookmarkPerfumeDto(perfume.getPerfume_id(), perfume.getPerfume_name(), perfume.getBrand_name());
            perfumes.add(bookmarkPerfumeDto);
        }
        return perfumes;
    }
}
