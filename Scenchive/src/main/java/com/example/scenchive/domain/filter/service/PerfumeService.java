package com.example.scenchive.domain.filter.service;

import com.example.scenchive.domain.board.service.S3Uploader;
import com.example.scenchive.domain.filter.dto.*;
import com.example.scenchive.domain.filter.repository.*;
import com.example.scenchive.domain.info.repository.PerfumenoteRepository;
import com.example.scenchive.domain.info.repository.PerfumescentRepository;
import com.example.scenchive.domain.review.repository.ReviewRepository;
import com.example.scenchive.domain.review.service.ReviewService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class PerfumeService {
    private final PerfumeTagRepository perfumeTagRepository;
    private final PerfumeRepository perfumeRepository;
    private final BrandRepository brandRepository;
    private final PTagRepository pTagRepository;
    private PerfumescentRepository perfumescentRepository;
    private PerfumenoteRepository perfumenoteRepository;
    private ReviewRepository reviewRepository;
    private final ReviewService reviewService;
    private final S3Uploader s3Uploader;

    private final String bucket = "https://scenchive.s3.ap-northeast-2.amazonaws.com/perfume/";


    @Autowired
    public PerfumeService(PerfumeTagRepository perfumeTagRepository, PerfumeRepository perfumeRepository,
                          BrandRepository brandRepository, PTagRepository pTagRepository,
                          PerfumescentRepository perfumescentRepository, PerfumenoteRepository perfumenoteRepository,
                          ReviewRepository reviewRepository, ReviewService reviewService, S3Uploader s3Uploader) {

        this.perfumeTagRepository = perfumeTagRepository;
        this.perfumeRepository = perfumeRepository;
        this.brandRepository = brandRepository;
        this.pTagRepository = pTagRepository;
        this.perfumescentRepository = perfumescentRepository;
        this.perfumenoteRepository = perfumenoteRepository;
        this.reviewRepository=reviewRepository;
        this.reviewService=reviewService;
        this.s3Uploader=s3Uploader;
    }

    @Transactional(readOnly = true)
    // 키워드 필터링 결과로 나온 향수 리스트 조회
    public List<PerfumeDto> getPerfumesByKeyword(List<PTag> keywordIds, Pageable pageable) {
        // 주어진 키워드 id들로 PerfumeTag 리스트 조회
        List<PerfumeTag> perfumeTags = perfumeTagRepository.findByPtagIn(keywordIds);
        // perfume_id 오름차순으로 조회
        //Set<Perfume> uniquePerfumes = new TreeSet<>((p1, p2) -> p1.getId().compareTo(p2.getId())); // Set: 다중 키워드로 인해 중복된 향수가 있는 경우 제거

        // 모든 태그가 있는 향수를 찾기 위해 태그 개수를 세어 일치하는 향수를 찾음
        Map<Perfume, Long> tagCountMap = new HashMap<>();
        for (PerfumeTag perfumeTag : perfumeTags) {
            Perfume perfume = perfumeTag.getPerfume();
            Long count = tagCountMap.getOrDefault(perfume, 0L);
            tagCountMap.put(perfume, count + 1);
        }

        // 모든 태그를 가진 향수들을 Set으로 필터링
        Set<Perfume> uniquePerfumes = tagCountMap.entrySet().stream()
                .filter(entry -> entry.getValue() == keywordIds.size()) // 모든 태그가 있는 경우 필터링
                .map(Map.Entry::getKey)
                .collect(Collectors.toSet());

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
            String perfumeImage = bucket + cleanedFileName + ".jpg";
            Brand brand = brandRepository.findById(perfume.getBrandId()).orElse(null);
            String brandName = (brand != null) ? brand.getBrandName() : null;
            String brandName_kr = (brand != null) ? brand.getBrandName_kr() : null;

            double ratingAvg=0.0;

            if (reviewRepository.findByPerfumeIdOrderByCreatedAtDesc(perfume.getId()).size()!=0){
                ratingAvg=reviewService.calculatePerfumeRating(perfume.getId()).getRatingAvg();
            }

            PerfumeDto perfumeDto = new PerfumeDto(perfume.getId(), perfume.getPerfumeName(), perfume.getPerfume_kr(), perfumeImage, brandName, brandName_kr, perfumeKeywordIds, ratingAvg);
            perfumes.add(perfumeDto);
        }

        perfumes=perfumes.stream().sorted(Comparator.comparing(PerfumeDto::getRatingAvg).reversed()).collect(Collectors.toList());

        // 향수 DTO 리스트 반환
        return perfumes;
    }

    @Transactional(readOnly = true)
    // 키워드 필터링 결과로 나온 전체 향수 개수 구하기
    public int getTotalPerfumeCount(List<PTag> keywordIds) {
        List<PerfumeTag> perfumeTags = perfumeTagRepository.findByPtagIn(keywordIds);
        //Set<Perfume> uniquePerfumes = new HashSet<>();

        Map<Perfume, Long> tagCountMap = new HashMap<>();
        for (PerfumeTag perfumeTag : perfumeTags) {
            Perfume perfume = perfumeTag.getPerfume();
            Long count = tagCountMap.getOrDefault(perfume, 0L);
            tagCountMap.put(perfume, count + 1);
        }

        // 모든 태그를 가진 향수들을 Set으로 필터링
        Set<Perfume> uniquePerfumes = tagCountMap.entrySet().stream()
                .filter(entry -> entry.getValue() == keywordIds.size()) // 모든 태그가 있는 경우 필터링
                .map(Map.Entry::getKey)
                .collect(Collectors.toSet());
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

    @Transactional(readOnly = true)
    public PerfumeFullInfoDto getPerfumeFullInfo(Long perfumeId) {
        Optional<Perfume> optionalPerfume  = perfumeRepository.findById(perfumeId);
        PerfumeFullInfoDto perfumeDto = optionalPerfume.map(perfume -> {
            String cleanedFileName = perfume.getPerfumeName().replaceAll("[^\\w]", "");
            String perfumeImage = bucket + cleanedFileName + ".jpg";
            Brand brand = brandRepository.findById(perfume.getBrandId()).orElse(null);

            String brandImageFile=null;
            String brandImage=null;

            if(brand!=null){
                brandImageFile = brand.getBrandName().replaceAll("[^\\w]", "");
                brandImage = bucket + brandImageFile + ".jpg";
            }

            return new PerfumeFullInfoDto(perfume.getId(), perfume.getPerfumeName(), perfume.getPerfume_kr(), perfumeImage,
                    brand != null ? brand.getBrandName() : null, brand != null ? brand.getBrandName_kr() : null, brand != null ? brandImage : null);
        }).orElse(null);

        return perfumeDto;
    }

    //브랜드 저장
    @Transactional
    public Brand saveBrand(BrandRegDto dto, MultipartFile image) {
        Brand brand = new Brand(dto.getBrandName(), dto.getBrandName_kr());
        String imageUrl = null;

        if (image != null && !image.isEmpty()) {
            try{
                imageUrl = s3Uploader.upload(image, "brand", dto.getBrandName()); //brand 이름의 디렉토리 생성 후 그 안에 파일 저장
            }
            catch (IOException e){
                e.printStackTrace();
            }
        }

        return brandRepository.save(brand);
    }

    //브랜드 삭제
    @Transactional
    public void deleteBrandById(Long id) {
        brandRepository.deleteBrandById(id);
    }

    // 향수 추가
    @Transactional
    public Perfume savePerfume(PerfumeSaveDto dto, MultipartFile image) {
        String imageUrl = null;

        if (image != null && !image.isEmpty()) {
            try{
                imageUrl = s3Uploader.upload(image, "perfume", dto.getPerfumeName()); //brand 이름의 디렉토리 생성 후 그 안에 파일 저장
            }
            catch (IOException e){
                e.printStackTrace();
            }
        }
        Brand brand = brandRepository.findBrandByBrandName(dto.getBrandName());
        Perfume perfume = new Perfume(dto.getPerfumeName(), dto.getPerfume_kr(), brand.getId());
        return perfumeRepository.save(perfume);
    }

    //향수 삭제
    @Transactional
    public void deletePerfumeById(Long id) {
        perfumeRepository.deleteById(id);
    }
}
