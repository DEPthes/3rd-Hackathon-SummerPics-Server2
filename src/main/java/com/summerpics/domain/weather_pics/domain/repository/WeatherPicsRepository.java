package com.summerpics.domain.weather_pics.domain.repository;

import com.summerpics.domain.weather_pics.domain.WeatherPics;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WeatherPicsRepository extends JpaRepository<WeatherPics, Long> {
}
