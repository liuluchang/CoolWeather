package com.example.liuluchang.coolweather.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Author: liuluchang
 * Time:  2018/9/12
 * Description: This is Basic
 */

public class Basic {

    @SerializedName("city")
    public String cityName;

    @SerializedName("id")
    public String weatherId;

    public Update update;

    public static class Update    {

        @SerializedName("loc")
        public String updateTime;
    }
}
