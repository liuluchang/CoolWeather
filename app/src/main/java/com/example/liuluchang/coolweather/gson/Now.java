package com.example.liuluchang.coolweather.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Author: liuluchang
 * Time:  2018/9/12
 * Description: This is Now
 */

public class Now {
    @SerializedName("tmp")
    public String temperature;

    @SerializedName("cond")
    public More more;

    public class More {

        @SerializedName("txt")
        public String info;

    }

}
