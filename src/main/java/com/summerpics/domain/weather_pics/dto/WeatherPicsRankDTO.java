package com.summerpics.domain.weather_pics.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WeatherPicsRankDTO {
    private Long picture_id;
    private String pictureUrl;
    private int thums;
    private String title;
}
