package com.summerpics.domain.weather_pics.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ThumsReq {
    private long picture_id;
    private boolean like;   // true : like, false : unlike
}
