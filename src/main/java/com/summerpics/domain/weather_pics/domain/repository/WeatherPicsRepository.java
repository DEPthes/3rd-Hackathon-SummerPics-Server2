package com.summerpics.domain.weather_pics.domain.repository;

import com.summerpics.domain.temp_info.domain.TempLabel;
import com.summerpics.domain.weather_info.domain.WeatherLabel;
import com.summerpics.domain.weather_pics.domain.WeatherPics;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WeatherPicsRepository extends JpaRepository<WeatherPics, Long> {
    @Query("SELECT wp FROM WeatherPics wp WHERE wp.thums > 0 ORDER BY wp.thums DESC")
    List<WeatherPics> findTopRankings();
    List<WeatherPics> findByTempInfoTempLabelAndWeatherInfoWeatherLabel(TempLabel tempLabel, WeatherLabel weatherLabel);
}
