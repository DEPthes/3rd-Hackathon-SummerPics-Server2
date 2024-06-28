
package com.summerpics.domain.weather_pics.application;

import com.summerpics.domain.temp_info.domain.TempLabel;
import com.summerpics.domain.weather_info.domain.WeatherLabel;
import com.summerpics.domain.weather_pics.domain.WeatherPics;
import com.summerpics.domain.weather_pics.domain.repository.WeatherPicsRepository;
import com.summerpics.domain.weather_pics.dto.request.WeatherInfoReq;
import com.summerpics.domain.weather_pics.dto.response.WeatherInfoRes;
import com.summerpics.domain.weather_pics.dto.response.WeatherPicRes;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class WeatherPicsService {

    @Value("${weather.api.url}")
    private String apiUrl;

    @Value("${weather.api.serviceKey}")
    private String serviceKey;

    private final WeatherPicsRepository weatherPicsRepository;

    @Transactional
    public WeatherPicRes getWeatherPic(WeatherInfoReq request) throws IOException, ParseException {
        // 확인용 로그
        System.out.println("apiUrl: " + apiUrl);
        System.out.println("serviceKey: " + serviceKey);
        WeatherInfoRes weatherInfoRes = getWeather(request.getBaseDate(), request.getBaseTime(), request.getNx(), request.getNy());
        System.out.println("Weather Info: " + weatherInfoRes); // 로그 추가
        String weatherStatus = calculateWeatherStatus(weatherInfoRes.getTempLabel(), weatherInfoRes.getWeatherLabel());
        String picUrl = determineImage(weatherInfoRes.getTempLabel(), weatherInfoRes.getWeatherLabel());
        return WeatherPicRes.builder()
                .picUrl(picUrl)
                .weatherStatus(weatherStatus)
                .temp((int) Math.round(weatherInfoRes.getTemperature()))  // 온도값 설정 (반올림하여 정수형으로 변환)
                .build();
    }

    private WeatherInfoRes getWeather(String baseDate, String baseTime, int x, int y) throws IOException, ParseException {
        String type = "json";  // 타입 (xml, json 등)
        String numOfRows = "100";    // 한 페이지 결과 수
        String pageNo = "1"; // 페이지 번호
        String nx=String.valueOf(x);
        String ny=String.valueOf(y);

        // URL 빌더
        StringBuilder urlBuilder = new StringBuilder(apiUrl);
        urlBuilder.append("?" + URLEncoder.encode("serviceKey", "UTF-8") + "=" + serviceKey);
        urlBuilder.append("&" + URLEncoder.encode("nx", "UTF-8") + "=" + URLEncoder.encode(nx, "UTF-8")); // 경도
        urlBuilder.append("&" + URLEncoder.encode("ny", "UTF-8") + "=" + URLEncoder.encode(ny, "UTF-8")); // 위도
        urlBuilder.append("&" + URLEncoder.encode("base_date", "UTF-8") + "=" + URLEncoder.encode(baseDate, "UTF-8")); // 조회 날짜
        urlBuilder.append("&" + URLEncoder.encode("base_time", "UTF-8") + "=" + URLEncoder.encode(baseTime, "UTF-8")); // 조회 시간
        urlBuilder.append("&" + URLEncoder.encode("dataType", "UTF-8") + "=" + URLEncoder.encode(type, "UTF-8")); // 타입
        urlBuilder.append("&" + URLEncoder.encode("numOfRows", "UTF-8") + "=" + URLEncoder.encode(numOfRows, "UTF-8")); // 한 페이지 결과 수
        urlBuilder.append("&" + URLEncoder.encode("pageNo", "UTF-8") + "=" + URLEncoder.encode(pageNo, "UTF-8")); // 페이지 번호

        // GET 요청
        URL url = new URL(urlBuilder.toString());
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Content-type", "application/json");
        System.out.println("Response code: " + conn.getResponseCode());
        BufferedReader rd;
        if (conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
            rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        } else {
            rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
        }
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = rd.readLine()) != null) {
            sb.append(line);
        }
        rd.close();
        conn.disconnect();
        String result = sb.toString();

        // JSON 파싱
        JSONParser parser = new JSONParser();
        JSONObject obj = (JSONObject) parser.parse(result);
        JSONObject parse_response = (JSONObject) obj.get("response");
        JSONObject parse_body = (JSONObject) parse_response.get("body");
        JSONObject parse_items = (JSONObject) parse_body.get("items");
        JSONArray parse_item = (JSONArray) parse_items.get("item");

        // 최신 fcstTime 찾기
        String latestFcstTime = "";
        for (int i = 0; i < parse_item.size(); i++) {
            JSONObject item = (JSONObject) parse_item.get(i);
            String fcstTime = item.get("fcstTime").toString();
            if (fcstTime.compareTo(latestFcstTime) > 0) {
                latestFcstTime = fcstTime;
            }
        }

        // 최신 fcstTime의 데이터만 필터링
        double temperature = 0;
        String skyCondition = "";
        String rainCondition = "";
        for (int i = 0; i < parse_item.size(); i++) {
            JSONObject weather = (JSONObject) parse_item.get(i);
            String fcstTime = weather.get("fcstTime").toString();

            // 최신 fcstTime인 데이터만 필터링
            if (!fcstTime.equals(latestFcstTime)) {
                continue;
            }

            String category = (String) weather.get("category");
            Object fcstValue = weather.get("fcstValue");

            // 기온, 하늘상태, 강수형태 저장
            if (category.equals("T1H")) {
                temperature = Double.parseDouble(fcstValue.toString());
            } else if (category.equals("SKY")) {
                skyCondition = fcstValue.toString();
            } else if (category.equals("PTY")) {
                rainCondition = fcstValue.toString();
            }
        }

        // 로그 추가
        System.out.println("Temperature: " + temperature);
        System.out.println("Sky Condition: " + skyCondition);
        System.out.println("Rain Condition: " + rainCondition);

        // 라벨 결정
        TempLabel tempLabel = determineTempLabel(temperature);
        WeatherLabel weatherLabel = determineWeatherLabel(skyCondition, rainCondition);

        return WeatherInfoRes.builder()
                .tempLabel(tempLabel)
                .weatherLabel(weatherLabel)
                .temperature(temperature)  // 온도값 설정
                .build();
    }

    private TempLabel determineTempLabel(double temperature) {
        if (temperature >= 28) {
            return TempLabel.LEVEL4;
        } else if (temperature >= 24) {
            return TempLabel.LEVEL3;
        } else if (temperature >= 20) {
            return TempLabel.LEVEL2;
        } else if (temperature >= 17) {
            return TempLabel.LEVEL1;
        } else {
            return TempLabel.LEVEL0;
        }
    }

    private WeatherLabel determineWeatherLabel(String skyCondition, String rainCondition) {
        if (!rainCondition.equals("0")) {
            return WeatherLabel.RAINY;
        } else if (skyCondition.equals("1")) {
            return WeatherLabel.SUNNY;
        } else {
            return WeatherLabel.CLOUDY;
        }
    }

    private String determineImage(TempLabel tempLabel, WeatherLabel weatherLabel) {
        List<WeatherPics> weatherPics = weatherPicsRepository.findByTempInfoTempLabelAndWeatherInfoWeatherLabel(tempLabel, weatherLabel);
        if (weatherPics.isEmpty()) {
            throw new EntityNotFoundException("No weather picture found for the given labels.");
        }
        Random rand = new Random();
        return weatherPics.get(rand.nextInt(weatherPics.size())).getPictureUrl();
    }

    private String calculateWeatherStatus(TempLabel tempLabel, WeatherLabel weatherLabel) {
        String weatherStatus = "";
        switch (tempLabel) {
            case LEVEL4:
                if (weatherLabel == WeatherLabel.SUNNY) {
                    weatherStatus = "무더위";
                } else if (weatherLabel == WeatherLabel.CLOUDY) {
                    weatherStatus = "무더위";
                } else if (weatherLabel == WeatherLabel.RAINY) {
                    weatherStatus = "매우 덥고 강한 비";
                }
                break;
            case LEVEL3:
                if (weatherLabel == WeatherLabel.SUNNY) {
                    weatherStatus = "후덥지근";
                } else if (weatherLabel == WeatherLabel.CLOUDY) {
                    weatherStatus = "덥고 흐림";
                } else if (weatherLabel == WeatherLabel.RAINY) {
                    weatherStatus = "후덥지근";
                }
                break;
            case LEVEL2:
                if (weatherLabel == WeatherLabel.SUNNY) {
                    weatherStatus = "시원함";
                } else if (weatherLabel == WeatherLabel.CLOUDY) {
                    weatherStatus = "흐림";
                } else if (weatherLabel == WeatherLabel.RAINY) {
                    weatherStatus = "강한 비";
                }
                break;
            case LEVEL1:
                if (weatherLabel == WeatherLabel.SUNNY) {
                    weatherStatus = "쌀쌀함";
                } else if (weatherLabel == WeatherLabel.CLOUDY) {
                    weatherStatus = "춥고 흐림";
                } else if (weatherLabel == WeatherLabel.RAINY) {
                    weatherStatus = "춥고 강한 비";
                }
//                break;
//            case LEVEL0:
//                if (weatherLabel == WeatherLabel.SUNNY) {
//                    weatherStatus = "매우 추움";
//                } else if (weatherLabel == WeatherLabel.CLOUDY) {
//                    weatherStatus = "매우 추움";
//                } else if (weatherLabel == WeatherLabel.RAINY) {
//                    weatherStatus = "매우 추움";
//                }
                break;
        }
        return weatherStatus;
    }
}
