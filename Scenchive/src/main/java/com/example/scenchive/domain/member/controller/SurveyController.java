package com.example.scenchive.domain.member.controller;

import com.example.scenchive.domain.member.dto.SurveyUserTagDto;
import com.example.scenchive.domain.member.repository.UserTag;
import com.example.scenchive.domain.member.service.SurveyService;
import lombok.RequiredArgsConstructor;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class SurveyController {
    private final SurveyService surveyService;

    @PostMapping("/survey")
    public List<UserTag> survey(@RequestBody SurveyUserTagDto surveyUserTagDto, Model model){
        model.addAttribute("survey", surveyService.surveySave(surveyUserTagDto));
        return surveyService.surveySave(surveyUserTagDto);
    }

   // @GetMapping("/survey")

}
