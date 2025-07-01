package com.example.scenchive.domain.member.controller;

import com.example.scenchive.domain.member.dto.ResponseSurveyUserTagDto;
import com.example.scenchive.domain.member.dto.SurveyUserTagDto;
import com.example.scenchive.domain.member.dto.UtagDto;
import com.example.scenchive.domain.member.repository.UserTag;
import com.example.scenchive.domain.member.repository.Utag;
import com.example.scenchive.domain.member.service.SurveyService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.List;

@RequiredArgsConstructor
@RestController
@CrossOrigin(origins = {"http://localhost:3000", "https://scenchive.github.io/"}, allowCredentials = "true", allowedHeaders = "Authorization")
public class SurveyController {
    private final SurveyService surveyService;

    //회원가입시 입력한 키워드 저장하기
    @Operation(summary = "공개 API", security = {})
    @PostMapping("/survey")
    public List<ResponseSurveyUserTagDto> survey(@RequestBody SurveyUserTagDto surveyUserTagDto){
        return surveyService.surveySave(surveyUserTagDto);
    }

    //회원가입 설문조사 키워드 조회
    @Operation(summary = "공개 API", security = {})
    @GetMapping("/survey")
    public List<UtagDto> survey(){
        return surveyService.surveyGetUtags();
    }
}
