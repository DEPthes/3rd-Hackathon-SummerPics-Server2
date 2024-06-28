package com.summerpics.domain.weather_info.domain;

import com.summerpics.domain.weather_pics.domain.WeatherPics;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name="WeatherInfo")
@NoArgsConstructor
@Getter
public class WeatherInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="weather_id", updatable = false)
    private Long weatherId;

    @Enumerated(EnumType.STRING)
    private WeatherLabel weatherLabel;

    @Column(name="cloudy_value")
    private int cloudyValue;

    @Column(name="rainy_value")
    private int rainyValue;

    @OneToMany(mappedBy = "weatherInfo")
    private List<WeatherPics> weatherPics;

    @Builder
    public WeatherInfo(Long weatherId, WeatherLabel weatherLabel, int cloudyValue, int rainyValue) {
        this.weatherId = weatherId;
        this.weatherLabel = weatherLabel;
        this.cloudyValue = cloudyValue;
        this.rainyValue = rainyValue;
    }



}
