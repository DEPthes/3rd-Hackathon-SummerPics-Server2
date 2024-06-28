package com.summerpics.domain.temp_info.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum TempLabel {
    LEVEL0("0"),
    LEVEL1("1"),
    LEVEL2("2"),
    LEVEL3("3"),
    LEVEL4("4");

    private String value;
}
