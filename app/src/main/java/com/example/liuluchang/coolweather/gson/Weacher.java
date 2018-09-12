package com.example.liuluchang.coolweather.gson;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Author: liuluchang
 * Time:  2018/9/7
 * Description: This is Weacher
 */

public class Weacher {

    public String status;

    public Basic basic;

    public AQI aqi;

    public Now now;

    public Suggestion suggestion;

    @SerializedName("daily_forecast")
    public List<Forecast> forecastList;

}
