package com.example.scenchive.domain.filter.service;

import com.example.scenchive.domain.filter.dto.BrandDto;
import com.example.scenchive.domain.filter.dto.PerfumeDto;
import com.example.scenchive.domain.filter.dto.SearchListDto;
import com.example.scenchive.domain.filter.dto.SearchPerfumeDto;
import com.example.scenchive.domain.filter.repository.Brand;
import com.example.scenchive.domain.filter.repository.BrandRepository;
import com.example.scenchive.domain.filter.repository.Perfume;
import com.example.scenchive.domain.filter.repository.PerfumeRepository;
import com.example.scenchive.domain.filter.utils.DeduplicationUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
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
                String cleanedFileName = perfume.getPerfumeName().replaceAll("[^\\w]", "");
                String perfumeImage = "https://scenchive.s3.ap-northeast-2.amazonaws.com/perfume/" + cleanedFileName + ".jpg";
                SearchPerfumeDto searchPerfumeDto = new SearchPerfumeDto(perfume.getId(), perfume.getPerfumeName(),
                        perfumeImage, brand.getId(), brand.getBrandName(), brand.getBrandName_kr());
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
            SearchPerfumeDto searchPerfumeDto = new SearchPerfumeDto(perfume.getPerfumeId(), perfume.getPerfumeName(),
                    perfumeImage, perfume.getBrandId(), brand.getBrandName(), brand.getBrandName_kr());
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
                SearchPerfumeDto searchPerfumeDto = new SearchPerfumeDto(perfume.getId(), perfume.getPerfumeName(),
                        perfumeImage, brand.getId(), brand.getBrandName(), brand.getBrandName_kr());
                searchPerfumeDtos.add(searchPerfumeDto);
            }
        }
        return searchPerfumeDtos.size();
    }


    //검색화면 : 향수 및 브랜드 조회
    public SearchListDto searchName(String name, Pageable pageable) {
        List<Perfume> perfumes = perfumeRepository.findByPerfumeNameContainingIgnoreCase(name); //검색어 포함된 향수 리스트
        List<Brand> brands = new ArrayList<>();

        if (name.matches(".*[ㄱ-ㅎㅏ-ㅣ가-힣]+.*")){
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
                if (brand != null) {
                    SearchPerfumeDto searchPerfumeDto = new SearchPerfumeDto(perfume.getId(), perfume.getPerfumeName(),
                            perfumeImage, brand.getId(), brand.getBrandName(), brand.getBrandName_kr());
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
                if (brand != null) {
                    SearchPerfumeDto searchPerfumeDto = new SearchPerfumeDto(perfume.getId(), perfume.getPerfumeName(),
                            perfumeImage, brand.getId(), brand.getBrandName(), brand.getBrandName_kr());
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
}