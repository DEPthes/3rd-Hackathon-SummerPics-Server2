package com.summerpics.domain.temp_info.domain;

import com.summerpics.domain.weather_pics.domain.WeatherPics;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.util.*;

@Entity
@Table(name="TempInfo")
@NoArgsConstructor
@Getter
public class TempInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="temp_id", updatable = false)
    private Long tempId;

    @Enumerated(EnumType.STRING)
    private TempLabel tempLabel;

    @Column(name="max_temp")
    private int maxTemp;

    @Column(name="min_temp")
    private int minTemp;

    @OneToMany(mappedBy = "tempInfo")
    private List<WeatherPics> weatherPics;

    @Builder
    public TempInfo(Long tempId, TempLabel tempLabel, int maxTemp, int minTemp) {
        this.tempId = tempId;
        this.tempLabel = tempLabel;
        this.maxTemp = maxTemp;
        this.minTemp = minTemp;
    }
}

