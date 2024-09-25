package com.example.scenchive.domain.info.dto;

import com.example.scenchive.domain.filter.repository.Perfume;
import com.example.scenchive.domain.info.repository.Perfumenote;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class PerfumescentDto {

    //private Perfume perfume;
    //private Perfumenote perfumenote;

    private Long perfumeId;
    private List<List<String>> scents;
//    private Long noteId;
//    private String scent;
//    private String scentKr;

    public PerfumescentDto(Long perfumeId, List<List<String>> scents){
        this.perfumeId = perfumeId;
        this.scents = scents;
    }

//    public PerfumescentDto(Long perfumeId, Long noteId, String scent, String scentKr){
//        this.perfumeId = perfumeId;
//        this.noteId = noteId;
//        this.scent = scent;
//        this.scentKr = scentKr;
//    }
//    public PerfumescentDto(Perfume perfume, Perfumenote perfumenote, String scent, String scentKr) {
//        this.perfume = perfume;
//        this.perfumenote = perfumenote;
//        this.scent = scent;
//        this.scentKr = scentKr;
//    }
}
