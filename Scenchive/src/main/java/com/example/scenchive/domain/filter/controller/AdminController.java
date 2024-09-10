package com.example.scenchive.domain.filter.controller;

import com.example.scenchive.domain.filter.dto.BrandRegDto;
import com.example.scenchive.domain.filter.dto.PerfumeSaveDto;
import com.example.scenchive.domain.filter.repository.Brand;
import com.example.scenchive.domain.filter.repository.Perfume;
import com.example.scenchive.domain.filter.service.PerfumeService;
import com.example.scenchive.domain.info.dto.PerfumescentDto;
import com.example.scenchive.domain.info.repository.Perfumescent;
import com.example.scenchive.domain.info.service.NotesService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@CrossOrigin(origins = {"http://localhost:3000", "https://scenchive.github.io/"}, allowCredentials = "true", allowedHeaders = "Authorization")
public class AdminController {
    private final PerfumeService perfumeService;
    private final NotesService notesService;
    public AdminController(PerfumeService perfumeService, NotesService notesService) {
        this.perfumeService = perfumeService;
        this.notesService = notesService;
    }

    //브랜드 추가
    @PostMapping("/master/brand")
//    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<?> saveBrand(@RequestPart BrandRegDto dto, @RequestPart(required = false) MultipartFile image) {
        try {
            Brand brand = perfumeService.saveBrand(dto, image);
            return new ResponseEntity<>(brand, HttpStatus.OK);
        } catch (Exception e) {
            return error();
        }
    }


    //브랜드 삭제
    @DeleteMapping("/master/brand")
//    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<?> deleteBrand(@RequestParam Long brandId) {
        try {
            perfumeService.deleteBrandById(brandId);
            return new ResponseEntity<>("성공", HttpStatus.OK);
        } catch (Exception e) {
            return error();
        }
    }

    //향수 추가
    @PostMapping("/master/perfume")
//    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<?> savePerfume(@RequestPart PerfumeSaveDto dto, @RequestPart(required = false) MultipartFile image) {
        try {
            Perfume perfume = perfumeService.savePerfume(dto, image);
            return new ResponseEntity<>(perfume, HttpStatus.OK);
        } catch (Exception e) {
            return error();
        }
    }

    // 노트 추가
    @PostMapping("/master/note")
    public ResponseEntity<?> saveNotes(@RequestBody PerfumescentDto dto) {
        try {
            //Perfumescent perfumescent = notesService.createNotes(dto);
            PerfumescentDto savedDto = notesService.createNotes(dto);
            return new ResponseEntity<>(savedDto, HttpStatus.OK);
        } catch (Exception e) {
            return error();
        }
    }

    //향수 삭제
    @DeleteMapping("/master/perfume")
//    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<?> deletePerfume(@RequestParam Long perfumeId) {
        try {
            perfumeService.deletePerfumeById(perfumeId);
            return new ResponseEntity<>("성공", HttpStatus.OK);
        } catch (Exception e) {
            return error();
        }
    }

    //에러 처리
    public ResponseEntity<?> error() {
        return new ResponseEntity<String>("서버 에러 발생", HttpStatus.INTERNAL_SERVER_ERROR);
    }
}