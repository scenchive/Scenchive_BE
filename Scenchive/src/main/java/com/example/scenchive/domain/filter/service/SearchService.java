package com.example.scenchive.domain.filter.service;

import com.example.scenchive.domain.filter.dto.SearchPerfumeDto;
import com.example.scenchive.domain.filter.repository.Brand;
import com.example.scenchive.domain.filter.repository.BrandRepository;
import com.example.scenchive.domain.filter.repository.Perfume;
import com.example.scenchive.domain.filter.repository.PerfumeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
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

    //검색화면 : 브랜드별 향수 리스트 조회
    public List<SearchPerfumeDto> brandPerfume(String name){
        List<Brand> brands = brandRepository.findByBrandName(name);
        List<SearchPerfumeDto> searchPerfumeDtos = new ArrayList<>();
        for (Brand brand : brands) {
            List<Perfume> perfumes = perfumeRepository.findByBrandId(brand.getId());
            for (Perfume perfume : perfumes) {
                SearchPerfumeDto searchPerfumeDto = new SearchPerfumeDto(perfume.getPerfumeName(), brand.getBrandName());
                searchPerfumeDtos.add(searchPerfumeDto);
            }
        }
        return searchPerfumeDtos;
    }

    //검색화면 : 향수 및 브랜드 조회
    public List<SearchPerfumeDto> searchName(String name) {
        List<Perfume> perfumes = perfumeRepository.findByPerfumeNameContainingIgnoreCase(name);
        List<SearchPerfumeDto> searchPerfumeDtos = new ArrayList<>();
        List<Brand> brands = brandRepository.findByBrandName(name);

        //브랜드 이름에서만 찾기
        if (perfumes.isEmpty()) {
            if (brands != null) {
                for (Brand brand : brands) {
                    perfumes = perfumeRepository.findByBrandId(brand.getId());
                    for (Perfume perfume : perfumes) {
                        SearchPerfumeDto searchPerfumeDto = new SearchPerfumeDto(perfume.getPerfumeName(), brand.getBrandName());
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
                    SearchPerfumeDto searchPerfumeDto = new SearchPerfumeDto(perfume.getPerfumeName(), brand.getBrandName());
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
                    SearchPerfumeDto searchPerfumeDto = new SearchPerfumeDto(perfume.getPerfumeName(), brand.getBrandName());
                    searchPerfumeDtos.add(searchPerfumeDto);
                } else {
                    throw new NullPointerException("검색하신 향수나 브랜드가 없습니다.");
                }
            }

            for (Brand brand : brands) {
                perfumes = perfumeRepository.findByBrandId(brand.getId());
                for (Perfume perfume : perfumes) {
                    SearchPerfumeDto searchPerfumeDto = new SearchPerfumeDto(perfume.getPerfumeName(), brand.getBrandName());
                    searchPerfumeDtos.add(searchPerfumeDto);
                }
            }
        }
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