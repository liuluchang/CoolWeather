package com.example.liuluchang.coolweather.gson;

/**
 * Author: liuluchang
 * Time:  2018/9/12
 * Description: This is AQI
 */

public class AQI {

    public AQICity city;

    public class AQICity{
        public String aqi;

        public String pm25;
    }

}
