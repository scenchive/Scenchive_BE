package com.example.scenchive.domain.filter.service;

import com.example.scenchive.domain.filter.dto.*;
import com.example.scenchive.domain.filter.repository.Brand;
import com.example.scenchive.domain.filter.repository.BrandRepository;
import com.example.scenchive.domain.filter.repository.Perfume;
import com.example.scenchive.domain.filter.repository.PerfumeRepository;
import com.example.scenchive.domain.filter.utils.DeduplicationUtils;
import com.example.scenchive.domain.info.dto.ScentAndScentKr;
import com.example.scenchive.domain.info.repository.Perfumenote;
import com.example.scenchive.domain.info.repository.PerfumenoteRepository;
import com.example.scenchive.domain.info.repository.Perfumescent;
import com.example.scenchive.domain.info.repository.PerfumescentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Transactional(readOnly = true)
@Service
public class SearchService {

    private final PerfumeRepository perfumeRepository;
    private final BrandRepository brandRepository;
    private final PerfumenoteRepository perfumenoteRepository;
    private final PerfumescentRepository perfumescentRepository;
    private int totalNotePerfumeCount = 0;

    @Autowired
    public SearchService(PerfumeRepository perfumeRepository, BrandRepository brandRepository,
                         PerfumenoteRepository perfumenoteRepository, PerfumescentRepository perfumescentRepository) {
        this.perfumeRepository = perfumeRepository;
        this.brandRepository = brandRepository;
        this.perfumenoteRepository = perfumenoteRepository;
        this.perfumescentRepository = perfumescentRepository;
    }

    // 노트별 향으로 향수 리스트 조회
    public List<SearchPerfumeDto> notePerfume(NoteRequestDto requestDto, Pageable pageable) {
        List<String> topNoteList = requestDto.getTopNote();
        List<String> middleNoteList = requestDto.getMiddleNote();
        List<String> baseNoteList = requestDto.getBaseNote();

        Perfumenote topPerfumenote = perfumenoteRepository.findById(1L).get(); // 탑노트 정보
        Perfumenote middlePerfumenote = perfumenoteRepository.findById(2L).get(); // 미들노트 정보
        Perfumenote basePerfumenote = perfumenoteRepository.findById(3L).get(); // 베이스노트 정보

        List<Perfumescent> searchTopnoteList = new ArrayList<>();
        List<Perfumescent> searchMiddlenoteList = new ArrayList<>();
        List<Perfumescent> searchBasenoteList = new ArrayList<>();

        List<Perfumescent> perfumeScents = new ArrayList<>();

//        if (!topNoteList.isEmpty() && !middleNoteList.isEmpty() && !baseNoteList.isEmpty()) {
        // 검색한 탑노트가 포함 되어있는 향수 리스트
        for (int i = 0; i < topNoteList.size(); i++) {
            searchTopnoteList = perfumescentRepository.findByPerfumenoteAndScentKrContaining(topPerfumenote, topNoteList.get(i));
        }

        // 검색한 미들노트가 포함 되어있는 향수 리스트
        for (int i = 0; i < middleNoteList.size(); i++) {
            searchMiddlenoteList = perfumescentRepository.findByPerfumenoteAndScentKrContaining(middlePerfumenote, middleNoteList.get(i));
        }

        // 검색한 베이스노트가 포함 되어있는 향수 리스트
        for (int i = 0; i < baseNoteList.size(); i++) {
            searchBasenoteList = perfumescentRepository.findByPerfumenoteAndScentKrContaining(basePerfumenote, baseNoteList.get(i));
//                        perfumeScents.addAll(perfumescentRepository.find3ByScentKrContaining(
//                                topPerfumenote, topNoteList.get(i),
//                                middlePerfumenote, middleNoteList.get(j),
//                                basePerfumenote, baseNoteList.get(k)));
        }

        if (searchTopnoteList.size() > 0) {
            perfumeScents.addAll(searchTopnoteList);
        }

        for (int i = 0; i < searchMiddlenoteList.size(); i++) {
            Perfumescent middleScent = searchMiddlenoteList.get(i);
            if (!perfumeScents.contains(middleScent)) {
                perfumeScents.add(middleScent);
            }
        }

        for (int i = 0; i < searchBasenoteList.size(); i++) {
            Perfumescent baseScent = searchBasenoteList.get(i);
            if (!perfumeScents.contains(baseScent)) {
                perfumeScents.add(baseScent);
            }
        }
//        if (searchTopnoteList != null) {
//            for (int i = 0; i < searchTopnoteList.size(); i++) {
//                Perfumescent topnote = searchTopnoteList.get(i);
//                if (searchMiddlenoteList.contains(topnote) && searchBasenoteList.contains(topnote)) {
//                    perfumeScents.add(topnote);
//                }
//            }
//        } else if (searchMiddlenoteList != null) {
//            for (int i = 0; i < searchMiddlenoteList.size(); i++) {
//                Perfumescent middlenote = searchMiddlenoteList.get(i);
//                if (searchBasenoteList.contains(middlenote)) {
//                    perfumeScents.add(middlenote);
//                }
//            }
//        }


//        } else if (!topNoteList.isEmpty() && !middleNoteList.isEmpty()) {   // 탑, 미들
//            for (int i = 0; i < topNoteList.size(); i++) {
//                for (int j = 0; j < middleNoteList.size(); j++) {
//                    perfumeScents.addAll(
//                            perfumescentRepository.find2ByScentKrContaining(
//                                    topPerfumenote, topNoteList.get(i),
//                                    middlePerfumenote, middleNoteList.get(j)));
//                }
//            }
//        } else if (!topNoteList.isEmpty() && !baseNoteList.isEmpty()) { // 탑, 베이스
//            for (int i = 0; i < topNoteList.size(); i++) {
//                for (int j = 0; j < baseNoteList.size(); j++) {
//                    perfumeScents.addAll(
//                            perfumescentRepository.find2ByScentKrContaining(
//                                    topPerfumenote, topNoteList.get(i),
//                                    basePerfumenote, baseNoteList.get(j)));
//                }
//            }
//        } else if (!middleNoteList.isEmpty() && !baseNoteList.isEmpty()) {  // 미들, 베이스
//            for (int i = 0; i < middleNoteList.size(); i++) {
//                for (int j = 0; j < baseNoteList.size(); j++) {
//                    perfumeScents.addAll(
//                            perfumescentRepository.find2ByScentKrContaining(
//                                    middlePerfumenote, middleNoteList.get(i),
//                                    basePerfumenote, baseNoteList.get(j)));
//                }
//            }
//        } else if (!topNoteList.isEmpty()) {    // 탑
//            for (int i = 0; i < topNoteList.size(); i++) {
//                perfumeScents.addAll(
//                        perfumescentRepository.findByPerfumenoteAndScentKrContaining(
//                                topPerfumenote, topNoteList.get(i)));
//            }
//        } else if (!middleNoteList.isEmpty()) { // 미들
//            for (int i = 0; i < middleNoteList.size(); i++) {
//                perfumeScents.addAll(
//                        perfumescentRepository.findByPerfumenoteAndScentKrContaining(
//                                middlePerfumenote, middleNoteList.get(i)));
//            }
//        } else if (!baseNoteList.isEmpty()) {    // 베이스
//            for (int i = 0; i < baseNoteList.size(); i++) {
//                perfumeScents.addAll(
//                        perfumescentRepository.findByPerfumenoteAndScentKrContaining(
//                                basePerfumenote, baseNoteList.get(i)));
//            }
//        }

        // 검색 결과 저장할 리스트
        List<SearchPerfumeDto> searchedPerfumes = new ArrayList<>();

        for (Perfumescent perfumescent : perfumeScents) {
            Perfume perfume = perfumescent.getPerfume();    // 향수 정보
            Brand brand = brandRepository.findById(perfume.getBrandId()).get(); // 브랜드 정보

            // 이미지
            String cleanedFileName = perfume.getPerfumeName().replaceAll("[^\\w]", "");
            String perfumeImage = "https://scenchive.s3.ap-northeast-2.amazonaws.com/perfume/" + cleanedFileName + ".jpg";
            String cleanedFileName2 = brand.getBrandName().replaceAll("[^\\w]", "");
            String brandImage = "https://scenchive.s3.ap-northeast-2.amazonaws.com/brand/" + cleanedFileName2 + ".jpg";

            SearchPerfumeDto searchPerfumeDto = new SearchPerfumeDto(
                perfume.getId(),
                perfume.getPerfumeName(),
                perfume.getPerfume_kr(),
                perfumeImage,
                perfume.getBrandId(),
                brand.getBrandName(),
                brand.getBrandName_kr(),
                brandImage
            );

            searchedPerfumes.add(searchPerfumeDto);
        }

        totalNotePerfumeCount = searchedPerfumes.size();

        // 페이징 처리
        int startIndex = (int) pageable.getOffset();
        int endIndex = Math.min(startIndex + pageable.getPageSize(), searchedPerfumes.size());

        return searchedPerfumes.subList(startIndex, endIndex);
    }

    public int getTotalNotePerfumeCount() {
        return totalNotePerfumeCount;
    }

    public int getTotalNoteCount(String noteValue){
        List<ScentAndScentKr> noteValueList = perfumescentRepository.findByScentContainingOrScentKrContaining(noteValue, noteValue);
        Set<String> scentSet = new HashSet<>();
        Set<String> scentKrSet = new HashSet<>();

        for(ScentAndScentKr value : noteValueList){
            scentSet.add(value.getScent());
            scentKrSet.add(value.getScentKr());
        }

        return scentSet.size();
    }

    // 검색화면 : 브랜드별 향수 리스트 조회
    public List<SearchPerfumeDto> brandPerfume(String name, Pageable pageable){
        // 주어진 브랜드 이름으로 brand 리스트 조회
        List<Brand> brands = brandRepository.findByBrandNameContainingIgnoreCase(name);
        List<SearchPerfumeDto> searchPerfumeDtos = new ArrayList<>();

        for (Brand brand : brands) {
            // 해당 브랜드 id를 갖는 향수 리스트 구하기
            List<Perfume> perfumes = perfumeRepository.findByBrandId(brand.getId());
            for (Perfume perfume : perfumes) {
                String cleanedFileName = perfume.getPerfumeName().replaceAll("[^\\w]", "");
                String perfumeImage = "https://scenchive.s3.ap-northeast-2.amazonaws.com/perfume/" + cleanedFileName + ".jpg";
                String cleanedFileName2 = brand.getBrandName().replaceAll("[^\\w]", "");
                String brandImage = "https://scenchive.s3.ap-northeast-2.amazonaws.com/brand/" + cleanedFileName2 + ".jpg";
                SearchPerfumeDto searchPerfumeDto = new SearchPerfumeDto(perfume.getId(), perfume.getPerfumeName(), perfume.getPerfume_kr(),
                        perfumeImage, brand.getId(), brand.getBrandName(), brand.getBrandName_kr(), brandImage);
                searchPerfumeDtos.add(searchPerfumeDto);
            }
        }
//        return searchPerfumeDtos;

        List<SearchPerfumeDto> perfumes = new ArrayList<>();

        int startIndex = (int) pageable.getOffset();
        int endIndex = Math.min(startIndex + pageable.getPageSize(), searchPerfumeDtos.size());

        List<SearchPerfumeDto> paginatedPerfumes = new ArrayList<>(searchPerfumeDtos).subList(startIndex, endIndex);

        for (SearchPerfumeDto perfume : paginatedPerfumes) {
            Brand brand=brandRepository.findById(perfume.getBrandId()).get();
            String cleanedFileName = perfume.getPerfumeName().replaceAll("[^\\w]", "");
            String perfumeImage = "https://scenchive.s3.ap-northeast-2.amazonaws.com/perfume/" + cleanedFileName + ".jpg";
            String cleanedFileName2 = brand.getBrandName().replaceAll("[^\\w]", "");
            String brandImage = "https://scenchive.s3.ap-northeast-2.amazonaws.com/brand/" + cleanedFileName2 + ".jpg";
            SearchPerfumeDto searchPerfumeDto = new SearchPerfumeDto(perfume.getPerfumeId(), perfume.getPerfumeName(), perfume.getPerfume_kr(),
                    perfumeImage, perfume.getBrandId(), brand.getBrandName(), brand.getBrandName_kr(), brandImage);
            perfumes.add(searchPerfumeDto);
        }

        return perfumes;
    }

    // 브랜드별 향수 전체 개수 조회
    public long getTotalBrandPerfumeCount(String name, Pageable pageable){
        List<Brand> brands = brandRepository.findByBrandNameContainingIgnoreCase(name);
        List<SearchPerfumeDto> searchPerfumeDtos = new ArrayList<>();
        for (Brand brand : brands) {
            List<Perfume> perfumes = perfumeRepository.findByBrandId(brand.getId());
            for (Perfume perfume : perfumes) {
                String cleanedFileName = perfume.getPerfumeName().replaceAll("[^\\w]", "");
                String perfumeImage = "https://scenchive.s3.ap-northeast-2.amazonaws.com/perfume/" + cleanedFileName + ".jpg";
                String cleanedFileName2 = brand.getBrandName().replaceAll("[^\\w]", "");
                String brandImage = "https://scenchive.s3.ap-northeast-2.amazonaws.com/brand/" + cleanedFileName2 + ".jpg";
                SearchPerfumeDto searchPerfumeDto = new SearchPerfumeDto(perfume.getId(), perfume.getPerfumeName(), perfume.getPerfume_kr(),
                        perfumeImage, brand.getId(), brand.getBrandName(), brand.getBrandName_kr(), brandImage);
                searchPerfumeDtos.add(searchPerfumeDto);
            }
        }
        return searchPerfumeDtos.size();
    }


    //검색화면 : 향수 및 브랜드 조회
    public SearchListDto searchName(String name, Pageable pageable) {
        List<Perfume> perfumes = perfumeRepository.findByPerfumeNameContainingIgnoreCase(name); //검색어 포함된 향수 리스트
        List<Brand> brands = new ArrayList<>();

        if (name.matches(".*[\\[\\]].*")){
            return new SearchListDto("잘못된 문자가 입력되었습니다.");
        }
        else if (name.matches(".*[ㄱ-ㅎㅏ-ㅣ가-힣]+.*")){
            brands = brandRepository.findByBrandNameKrContainingIgnoreCase(name);
        } else {
            brands = brandRepository.findByBrandNameContainingIgnoreCase(name); //검색어 포함된 브랜드 리스트
        }
        List<SearchPerfumeDto> searchPerfumeDtos = new ArrayList<>();
        List<BrandDto> brandDtos=new ArrayList<>();

        //브랜드 이름에서만 찾기
        if (perfumes.isEmpty()) {
            if (brands != null) {
                for (Brand brand : brands) {
                    String cleanedFileName = brand.getBrandName().replaceAll("[^\\w]", "");
                    String brandImage = "https://scenchive.s3.ap-northeast-2.amazonaws.com/brand/" + cleanedFileName + ".jpg";
                    BrandDto brandDto = new BrandDto(brand.getBrandName(), brand.getBrandName_kr(), brandImage);
                    brandDtos.add(brandDto);
                }
            } else {
                throw new NullPointerException("검색하신 향수나 브랜드가 없습니다.");
            }
        }

        //향수 이름에서만 찾기
        else if (perfumes != null && brands.isEmpty()) {
            for (Perfume perfume : perfumes) {
                Brand brand = brandRepository.findById(perfume.getBrandId()).orElse(null);
                String cleanedFileName = perfume.getPerfumeName().replaceAll("[^\\w]", "");
                String perfumeImage = "https://scenchive.s3.ap-northeast-2.amazonaws.com/perfume/" + cleanedFileName + ".jpg";
                String cleanedFileName2 = brand.getBrandName().replaceAll("[^\\w]", "");
                String brandImage = "https://scenchive.s3.ap-northeast-2.amazonaws.com/brand/" + cleanedFileName2 + ".jpg";
                if (brand != null) {
                    SearchPerfumeDto searchPerfumeDto = new SearchPerfumeDto(perfume.getId(), perfume.getPerfumeName(), perfume.getPerfume_kr(),
                            perfumeImage, brand.getId(), brand.getBrandName(), brand.getBrandName_kr(), brandImage);
                    searchPerfumeDtos.add(searchPerfumeDto);
                } else {
                    throw new NullPointerException("검색하신 향수나 브랜드가 없습니다.");
                }
            }
        }

        //향수 이름+브랜드 이름에서 찾기
        else if (perfumes != null && brands!=null) {
            for (Perfume perfume : perfumes) {
                Brand brand = brandRepository.findById(perfume.getBrandId()).orElse(null);
                String cleanedFileName = perfume.getPerfumeName().replaceAll("[^\\w]", "");
                String perfumeImage = "https://scenchive.s3.ap-northeast-2.amazonaws.com/perfume/" + cleanedFileName + ".jpg";
                String cleanedFileName2 = brand.getBrandName().replaceAll("[^\\w]", "");
                String brandImage = "https://scenchive.s3.ap-northeast-2.amazonaws.com/brand/" + cleanedFileName2 + ".jpg";
                if (brand != null) {
                    SearchPerfumeDto searchPerfumeDto = new SearchPerfumeDto(perfume.getId(), perfume.getPerfumeName(), perfume.getPerfume_kr(),
                            perfumeImage, brand.getId(), brand.getBrandName(), brand.getBrandName_kr(), brandImage);
                    searchPerfumeDtos.add(searchPerfumeDto);
                } else {
                    throw new NullPointerException("검색하신 향수나 브랜드가 없습니다.");
                }
            }

            for (Brand brand : brands) {
                perfumes = perfumeRepository.findByBrandId(brand.getId());
                String cleanedFileName = brand.getBrandName().replaceAll("[^\\w]", "");
                String brandImage = "https://scenchive.s3.ap-northeast-2.amazonaws.com/brand/" + cleanedFileName + ".jpg";
                for (Perfume perfume : perfumes) {
                    BrandDto brandDto = new BrandDto(brand.getBrandName(), brand.getBrandName_kr(), brandImage);
                    brandDtos.add(brandDto);
                }
            }
        }
        // 중복 제거 및 페이지 결과 생성
        searchPerfumeDtos = new PageImpl<>(DeduplicationUtils.deduplication(searchPerfumeDtos, SearchPerfumeDto::getPerfumeName)).getContent();
        brandDtos = new PageImpl<>(DeduplicationUtils.deduplication(brandDtos, BrandDto::getBrandName)).getContent();

        int searchPerfumeDtosLen = searchPerfumeDtos.size();
        int brandDtoslen = brandDtos.size();

        // 페이징 처리
        int page = pageable.getPageNumber(); // 현재 페이지 번호
        int pageSize = pageable.getPageSize(); // 페이지 크기

        int start = page * pageSize;
        int end = Math.min((start + pageSize), searchPerfumeDtos.size());
        List<SearchPerfumeDto> paginatedSearchPerfumeDtos = searchPerfumeDtos.subList(start, end);
        searchPerfumeDtos = DeduplicationUtils.deduplication(paginatedSearchPerfumeDtos, SearchPerfumeDto::getPerfumeName);

        SearchListDto searchListDto = new SearchListDto(brandDtoslen, brandDtos, searchPerfumeDtosLen, searchPerfumeDtos);
        return searchListDto;
    }

    // 노트 정보 가져오기
    public List<NoteValueDto> noteValue(String noteValue, Pageable pageable) {
        List<ScentAndScentKr> noteValueList = perfumescentRepository.findByScentContainingOrScentKrContaining(noteValue, noteValue);

        List<NoteValueDto> dtoList = new ArrayList<>();
        Set<String> scentSet = new HashSet<>(); //
        Set<String> scentKrSet = new HashSet<>(); //

        // 중복 값 제거
        for (ScentAndScentKr value: noteValueList) {
            String scent = value.getScent();
            String scentKr = value.getScentKr();
            if (!scentSet.contains(scent) && !scentKrSet.contains(scentKr)) {
                scentSet.add(scent);
                scentKrSet.add(scentKr);

                NoteValueDto noteDto = new NoteValueDto(scent, scentKr);
                dtoList.add(noteDto);
            }
        }

        // 페이징 처리
        int startIndex = (int) pageable.getOffset();
        int endIndex = Math.min(startIndex + pageable.getPageSize(), dtoList.size());

        return dtoList.subList(startIndex, endIndex);
    }
}