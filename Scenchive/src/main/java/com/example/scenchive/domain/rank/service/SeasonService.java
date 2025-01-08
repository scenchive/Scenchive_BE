package com.example.scenchive.domain.rank.service;

import com.example.scenchive.domain.rank.repository.Season;
import com.example.scenchive.domain.rank.repository.SeasonName;
import com.example.scenchive.domain.rank.repository.SeasonRepository;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class SeasonService {

    private SeasonRepository seasonRepository;

    // 데이터베이스 초기화
    @PostConstruct
    public void init() {
        // 이미 값이 있다면 추가하지 않도록 조건 추가
        if (seasonRepository.count() == 0) {
            for (SeasonName seasonName : SeasonName.values()) {
                seasonRepository.save(new Season(seasonName));
            }
        }
    }

    // 현재 계절 정보 가져오기
    public SeasonName getSeasonName(int month) {
        for (SeasonName name : SeasonName.values()) {
            if (isMonthInSeason(month, name)) {
                return name;
            }
        }
        return null;
    }

    // 월 정보를 받아 어떤 계절인지 확인
    boolean isMonthInSeason(int month, SeasonName season) {
        if (season.getStartMonth() <= season.getEndMonth()) {
            // 일반적인 순서 (예: SPRING(3~5))
            return month >= season.getStartMonth() && month <= season.getEndMonth();
        } else {
            // 연말~연초를 걸치는 경우 (예: WINTER(12~2))
            return month >= season.getStartMonth() || month <= season.getEndMonth();
        }
    }

}
