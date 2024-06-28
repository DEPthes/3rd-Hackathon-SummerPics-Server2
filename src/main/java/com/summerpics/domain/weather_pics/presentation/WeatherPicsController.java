package com.summerpics.domain.weather_pics.presentation;

import com.summerpics.domain.weather_pics.application.WeatherPicsService;
import com.summerpics.domain.weather_pics.dto.request.WeatherInfoReq;
import com.summerpics.domain.weather_pics.dto.response.WeatherPicRes;
import lombok.RequiredArgsConstructor;
import org.json.simple.parser.ParseException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/pic")
public class WeatherPicsController {

    private final WeatherPicsService weatherPicsService;

    @PostMapping("/")
    public ResponseEntity<WeatherPicRes> getWeatherPic(@RequestBody WeatherInfoReq request) throws IOException, ParseException {
        WeatherPicRes weatherPicRes = weatherPicsService.getWeatherPic(request);
        return ResponseEntity.ok(weatherPicRes);
    }
}

