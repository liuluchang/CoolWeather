package com.example.liuluchang.coolweather.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Author: liuluchang
 * Time:  2018/9/12
 * Description: This is Suggestion
 */

public class Suggestion {
    @SerializedName("comf")
    public Comfort comfort;

    @SerializedName("cw")
    public CarWash carWash;

    public Sport sport;

    public class Comfort {

        @SerializedName("txt")
        public String info;

    }

    public class CarWash {

        @SerializedName("txt")
        public String info;

    }

    public class Sport {

        @SerializedName("txt")
        public String info;

    }

}
