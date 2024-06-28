package com.summerpics.domain.weather_pics.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class WeatherInfoReq {
    private String baseDate;
    private String baseTime;
    private String nx;
    private String ny;
}
