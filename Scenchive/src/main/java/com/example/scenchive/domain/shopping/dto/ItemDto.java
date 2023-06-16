package com.example.scenchive.domain.shopping.dto;

import lombok.Getter;
import org.json.JSONObject;
import java.lang.Comparable;

@Getter
public class ItemDto implements Comparable<ItemDto> {
    private String title;
    private String link;
    private String image;
    private int lprice;

    private String mallName;

    public ItemDto(JSONObject itemJson) {
        this.title = itemJson.getString("title");
        this.link = itemJson.getString("link");
        this.image = itemJson.getString("image");
        this.lprice = itemJson.getInt("lprice");
        this.mallName = itemJson.getString("mallName");
    }

    @Override
    public int compareTo(ItemDto other) {
        // 현재 객체(this)의 lprice와 다른 객체(other)의 lprice를 비교하여 정렬 순서 결정
        return Integer.compare(this.lprice, other.lprice);
    }
}
