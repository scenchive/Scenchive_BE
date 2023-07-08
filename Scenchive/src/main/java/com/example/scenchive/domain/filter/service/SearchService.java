package com.example.scenchive.domain.filter.service;

import com.example.scenchive.domain.filter.dto.PerfumeDto;
import com.example.scenchive.domain.filter.dto.SearchPerfumeDto;
import com.example.scenchive.domain.filter.repository.Brand;
import com.example.scenchive.domain.filter.repository.BrandRepository;
import com.example.scenchive.domain.filter.repository.Perfume;
import com.example.scenchive.domain.filter.repository.PerfumeRepository;
import com.example.scenchive.domain.filter.utils.DeduplicationUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
@Service
public class SearchService {

    private final PerfumeRepository perfumeRepository;
    private final BrandRepository brandRepository;

    @Autowired
    public SearchService(PerfumeRepository perfumeRepository, BrandRepository brandRepository) {
        this.perfumeRepository = perfumeRepository;
        this.brandRepository = brandRepository;
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
                SearchPerfumeDto searchPerfumeDto = new SearchPerfumeDto(perfume.getId(), perfume.getPerfumeName(), brand.getId(), brand.getBrandName());
                searchPerfumeDtos.add(searchPerfumeDto);
            }
        }
//        return searchPerfumeDtos;

        List<SearchPerfumeDto> perfumes = new ArrayList<>();

        int startIndex = (int) pageable.getOffset();
        int endIndex = Math.min(startIndex + pageable.getPageSize(), searchPerfumeDtos.size());

        List<SearchPerfumeDto> paginatedPerfumes = new ArrayList<>(searchPerfumeDtos).subList(startIndex, endIndex);

        for (SearchPerfumeDto perfume : paginatedPerfumes) {
            Optional<Brand> brand = brandRepository.findById(perfume.getBrandId());
            String brandName = brand.get().getBrandName();
            SearchPerfumeDto searchPerfumeDto = new SearchPerfumeDto(perfume.getPerfumeId(), perfume.getPerfumeName(), perfume.getBrandId(), brandName);
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
                SearchPerfumeDto searchPerfumeDto = new SearchPerfumeDto(perfume.getId(), perfume.getPerfumeName(), brand.getId(), brand.getBrandName());
                searchPerfumeDtos.add(searchPerfumeDto);
            }
        }
        return searchPerfumeDtos.size();
    }


    //검색화면 : 향수 및 브랜드 조회
    public List<SearchPerfumeDto> searchName(String name, Pageable pageable) {
        List<Perfume> perfumes = perfumeRepository.findByPerfumeNameContainingIgnoreCase(name);
        List<SearchPerfumeDto> searchPerfumeDtos = new ArrayList<>();
        List<Brand> brands = brandRepository.findByBrandNameContainingIgnoreCase(name);

        //브랜드 이름에서만 찾기
        if (perfumes.isEmpty()) {
            if (brands != null) {
                for (Brand brand : brands) {
                    perfumes = perfumeRepository.findByBrandId(brand.getId());
                    for (Perfume perfume : perfumes) {
                        SearchPerfumeDto searchPerfumeDto = new SearchPerfumeDto(perfume.getId(), perfume.getPerfumeName(), brand.getId(), brand.getBrandName());
                        searchPerfumeDtos.add(searchPerfumeDto);
                    }
                }
            } else {
                throw new NullPointerException("검색하신 향수나 브랜드가 없습니다.");
            }
        }

        //향수 이름에서만 찾기
        else if (perfumes != null && brands.isEmpty()) {
            for (Perfume perfume : perfumes) {
                Brand brand = brandRepository.findById(perfume.getBrandId()).orElse(null);
                if (brand != null) {
                    SearchPerfumeDto searchPerfumeDto = new SearchPerfumeDto(perfume.getId(), perfume.getPerfumeName(),  brand.getId(), brand.getBrandName());
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
                if (brand != null) {
                    SearchPerfumeDto searchPerfumeDto = new SearchPerfumeDto(perfume.getId(), perfume.getPerfumeName(),  brand.getId(), brand.getBrandName());
                    searchPerfumeDtos.add(searchPerfumeDto);
                } else {
                    throw new NullPointerException("검색하신 향수나 브랜드가 없습니다.");
                }
            }

            for (Brand brand : brands) {
                perfumes = perfumeRepository.findByBrandId(brand.getId());
                for (Perfume perfume : perfumes) {
                    SearchPerfumeDto searchPerfumeDto = new SearchPerfumeDto(perfume.getId(), perfume.getPerfumeName(),  brand.getId(), brand.getBrandName());
                    searchPerfumeDtos.add(searchPerfumeDto);
                }
            }
        }
        searchPerfumeDtos= DeduplicationUtils.deduplication(searchPerfumeDtos, SearchPerfumeDto::getPerfumeName);
        return searchPerfumeDtos;
    }
}

//        else if(perfumes!= null && brands!= null){
//            for (Perfume perfume : perfumes) {
//                Brand brand = brandRepository.findById(perfume.getBrandId()).orElse(null);
//                if (brand != null) {
//                    SearchPerfumeDto searchPerfumeDto = new SearchPerfumeDto(perfume.getPerfumeName(), brand.getBrandName());
//                    searchPerfumeDtos.add(searchPerfumeDto);
//                } else {
//                    throw new NullPointerException("검색하신 향수나 브랜드가 없습니다.");
//                }
//            }
//            for (Brand brand : brands) {
//                perfumes = perfumeRepository.findByBrandId(brand.getId());
//                for (Perfume perfume : perfumes) {
//                    SearchPerfumeDto searchPerfumeDto = new SearchPerfumeDto(perfume.getPerfumeName(), brand.getBrandName());
//                    searchPerfumeDtos.add(searchPerfumeDto);
//                }
//            }
//        } else {
//            throw new NullPointerException("검색하신 향수나 브랜드가 없습니다.");
//        }