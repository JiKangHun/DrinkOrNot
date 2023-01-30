package com.ssafy.cadang.dto.data;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class WeekDataDto {

    private List<DayGraphDto> thisWeekGraphList;
    private int todayCaffe;
    private int todaySugar;
    private int dayCaffeGap;
    private int daySugarGap;
    private int thisWeekCaffe;
    private int thisWeekSugar;
    private int weekCaffeGap;
    private int weekSugarGap;

}
