package com.summerpics.domain.weather_pics.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name="WeatherPics")
@NoArgsConstructor
@Getter
public class WeatherPics {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="picture_id", updatable = false)
    private Long pictureId;

    @Column(name="picture_url")
    private String pictureUrl;

    @Column(name="thums")
    private int thums;

    @Column(name="title")
    private String title;


}
