package com.summerpics.domain.weather_pics.presentation;

import com.summerpics.domain.weather_pics.application.WeatherPicsService;
import com.summerpics.domain.weather_pics.dto.WeatherPicsRankDTO;
import com.summerpics.domain.weather_pics.dto.request.ThumsReq;
import com.summerpics.domain.weather_pics.dto.request.WeatherInfoReq;
import com.summerpics.domain.weather_pics.dto.response.ThumsRes;
import com.summerpics.domain.weather_pics.dto.response.WeatherPicRes;
import lombok.RequiredArgsConstructor;
import org.json.simple.parser.ParseException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

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

    // 클릭 시 좋아요수 증가
    @PostMapping("/thums")
    public ResponseEntity<ThumsRes> updateThums(@RequestBody ThumsReq thumsReq) {
        ThumsRes response = weatherPicsService.updateThums(thumsReq);
        return ResponseEntity.ok(response);
    }

    // 좋아요 취소
    @PostMapping("/thums/cancel")
    public ResponseEntity<ThumsRes> cancelThums(@RequestBody ThumsReq thumsReq) {
        ThumsRes response = weatherPicsService.cancelThums(thumsReq);
        return ResponseEntity.ok(response);
    }

    // 좋아요 수에 따른 랭킹
    @GetMapping("/rank")
    public List<WeatherPicsRankDTO> getWeatherPicsRank() {
        return weatherPicsService.getWeatherPicsRank();
    }
}

