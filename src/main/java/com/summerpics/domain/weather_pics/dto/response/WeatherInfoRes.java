package com.summerpics.domain.weather_pics.dto.response;

import com.summerpics.domain.temp_info.domain.TempLabel;
import com.summerpics.domain.weather_info.domain.WeatherLabel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class WeatherInfoRes {

    TempLabel tempLabel;
    WeatherLabel weatherLabel;
    double temperature;

}
