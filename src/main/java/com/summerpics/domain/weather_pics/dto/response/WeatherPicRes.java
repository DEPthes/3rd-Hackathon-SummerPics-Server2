package com.summerpics.domain.weather_pics.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class WeatherPicRes {
    private String picUrl;
    private String weatherStatus;
    private int temp;
}
