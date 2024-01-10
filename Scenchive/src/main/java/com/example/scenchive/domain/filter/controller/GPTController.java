//package com.example.scenchive.domain.filter.controller;
//
//import com.example.scenchive.domain.filter.service.GPTService;
//import com.fasterxml.jackson.core.JsonProcessingException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.MediaType;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.util.Collections;
//import java.util.Locale;

//@RestController
//@RequiredArgsConstructor
//public class GPTController {
//    private final GPTService gptService;
//
//    @PostMapping(value = "engToKr", produces = MediaType.APPLICATION_JSON_VALUE)
//    public void engToKr(Locale locale, HttpServletRequest request, HttpServletResponse response) {
//        try{
//            gptService.engToKr();
//        } catch (JsonProcessingException e) {
//            throw new RuntimeException(e);
//        }
//    }
//}
