package com.example.scenchive.domain.filter.service;

import com.example.scenchive.domain.filter.dto.MainPerfumeDto;
import com.example.scenchive.domain.filter.dto.PersonalDto;
import com.example.scenchive.domain.filter.repository.*;
import com.example.scenchive.domain.filter.utils.DeduplicationUtils;
import com.example.scenchive.domain.member.repository.*;
import com.example.scenchive.domain.review.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Transactional
@Service
public class PersonalService {
    private final UserTagRepository userTagRepository;
    private final PerfumeTagRepository perfumeTagRepository;
    private final MemberRepository memberRepository;
    private final BrandRepository brandRepository;
    private final PTagRepository pTagRepository;
    private final ReviewService reviewService;

    @Autowired
    public PersonalService(UserTagRepository userTagRepository, PerfumeTagRepository perfumeTagRepository,
                           MemberRepository memberRepository, BrandRepository brandRepository,
                           PTagRepository pTagRepository, ReviewService reviewService) {
        this.userTagRepository = userTagRepository;
        this.perfumeTagRepository = perfumeTagRepository;
        this.memberRepository = memberRepository;
        this.brandRepository = brandRepository;
        this.pTagRepository = pTagRepository;
        this.reviewService=reviewService;
    }


   //향수 프로필 화면 : 사용자 Id를 넘겨주면 추천 향수 리스트를 반환
    public List<PersonalDto> getPerfumesByUserKeyword(Long userId) {
        Member member = memberRepository.findById(userId).get(); //사용자 아이디로 사용자 객체 찾기
        List<UserTag> userTags = userTagRepository.findByMember(member); //사용자 객체로 유저별태그 리스트 반환

        List<PersonalDto> perfumes = new ArrayList<>();
        List<Long> keywordIds = new ArrayList<>();

        for (UserTag userTag : userTags) {
            Long utagId = userTag.getUtag().getId(); //유저태그에 저장돼있는 세부코드 아이디 가져오기
            keywordIds.add(utagId);
            PTag pTag = pTagRepository.findById(utagId).get(); //유저태그에 있는 세부코드 아이디와 같은 향수세부태그 가져오기
            List<PerfumeTag> perfumeTags = perfumeTagRepository.findByPtag(pTag); //향수세부태그를 가진 향수리스트 가져오기

            for (PerfumeTag perfumeTag : perfumeTags) { //향수리스트에 있는 향수 하나씩 꺼내기
                Perfume perfume = perfumeTag.getPerfume();
                String cleanedFileName = perfume.getPerfumeName().replaceAll("[^\\w]", "");
                String perfumeImage = "https://scenchive.s3.ap-northeast-2.amazonaws.com/perfume/" + cleanedFileName + ".jpg";
                Brand brand = brandRepository.findById(perfume.getBrandId()).orElse(null);
                String brandName = (brand != null) ? brand.getBrandName() : null;
                String brandName_kr = (brand != null) ? brand.getBrandName_kr() : null;
                PersonalDto personalDto = new PersonalDto(perfume.getId(), perfume.getPerfumeName(), perfumeImage, brandName, brandName_kr, keywordIds);
                perfumes.add(personalDto);
            }
        }

        // 향수 DTO 리스트 반환
        return perfumes;
    }

    //메인 화면 : 사용자 Id와 계절 Id를 넘겨주면 추천 향수 리스트를 반환
    public List<MainPerfumeDto> getPerfumesByUserAndSeason(Long userId, Long seasonId) {
        Member member = memberRepository.findById(userId).get(); //사용자 아이디로 사용자 객체 찾기
        List<UserTag> userTags = userTagRepository.findByMember(member); //사용자 객체로 유저별태그 리스트 반환

        List<MainPerfumeDto> perfumes = new ArrayList<>();
        List<Long> keywordIds = new ArrayList<>();

        //계절코드를 가진 향수 리스트 가져오기
        PTag seasonTag = pTagRepository.findById(seasonId).get();
        List<PerfumeTag> seasonPerfumeTags = perfumeTagRepository.findByPtag(seasonTag);

        //사용자가 선택한 세부코드를 가진 향수 리스트 가져오기
        for (UserTag userTag : userTags) {
            Long utagId = userTag.getUtag().getId(); //유저태그에 저장돼있는 세부코드 아이디 가져오기
            keywordIds.add(utagId);
            PTag pTag = pTagRepository.findById(utagId).get(); //유저태그에 있는 세부코드 아이디와 같은 향수세부태그 가져오기
            List<PerfumeTag> perfumeTags = perfumeTagRepository.findByPtag(pTag); //향수세부태그를 가진 향수리스트 가져오기

            for (PerfumeTag perfumeTag : perfumeTags) { //향수세부태그를 가진 향수리스트에 있는 향수 하나씩 꺼내기
                for (PerfumeTag seasonPerfumeTag : seasonPerfumeTags) { //계절코드를 가진 향수리스트에 있는 향수 하나씩 꺼내기
                    if (perfumeTag.getPerfumeId() == seasonPerfumeTag.getPerfumeId()) { //두 향수의 아이디가 같은 경우 향수 추가
                        Perfume perfume = perfumeTag.getPerfume();
                        String cleanedFileName = perfume.getPerfumeName().replaceAll("[^\\w ]", "");
                        String perfumeImage = "https://scenchive.s3.ap-northeast-2.amazonaws.com/perfume/" + cleanedFileName + ".jpg";
                        Brand brand = brandRepository.findById(perfume.getBrandId()).orElse(null);
                        String brandName = (brand != null) ? brand.getBrandName() : null;
                        String brandName_kr = (brand != null) ? brand.getBrandName_kr() : null;

                        double ratingAvg=reviewService.calculatePerfumeRating(perfume.getId()).getRatingAvg();

                        MainPerfumeDto mainPerfumeDto = new MainPerfumeDto(perfume.getId(), perfume.getPerfumeName(), perfumeImage, brandName, brandName_kr, keywordIds, ratingAvg);
                        if(!perfumes.contains(mainPerfumeDto)) {
                            perfumes.add(mainPerfumeDto);
                        }
                    }
                }
            }
        }

        Collections.sort(keywordIds);


        // 향수 DTO 리스트 반환
        perfumes = DeduplicationUtils.deduplication(perfumes, MainPerfumeDto::getPerfumeName);

        perfumes=perfumes.stream().sorted(Comparator.comparing(MainPerfumeDto::getRatingAvg).reversed()).collect(Collectors.toList());
        perfumes=perfumes.stream().skip(0).limit(3).collect(Collectors.toList());
        return perfumes;
    }
}