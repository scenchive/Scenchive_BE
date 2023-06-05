package com.example.scenchive.domain.shopping.controller;

import com.example.scenchive.domain.shopping.dto.ItemDto;
import com.example.scenchive.domain.shopping.utils.NaverShopSearch;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
@RestController
public class SearchRequestController {
    private final NaverShopSearch naverShopSearch;

    @GetMapping("/product/search")
    public List<ItemDto> getItems(@RequestParam String query) {
        String resultString = naverShopSearch.search(query);
        List<ItemDto> items = naverShopSearch.fromJSONtoItems(resultString);
        items = items.subList(0, Math.min(items.size(), 30)); // 상위 30개의 상품만 유지

        Collections.sort(items); // 최저가를 기준으로 상품 리스트 정렬

        return items;
    }
}
