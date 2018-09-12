package com.example.liuluchang.coolweather.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Author: liuluchang
 * Time:  2018/9/12
 * Description: This is Forecast
 */

public class Forecast {
    public String date;

    @SerializedName("tmp")
    public Temperature temperature;

    @SerializedName("cond")
    public More more;

    public class Temperature {

        public String max;

        public String min;

    }

    public class More {

        @SerializedName("txt_d")
        public String info;

    }
}
