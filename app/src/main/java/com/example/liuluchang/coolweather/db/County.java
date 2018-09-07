package com.example.liuluchang.coolweather.db;

import org.litepal.crud.DataSupport;

/**
 * Author: liuluchang
 * Time:  2018/9/7
 * Description: This is Weacher
 */

public class County extends DataSupport {

    private int id;

    private String countyName;

    private int countyCode;

    private int cityId;

    private int weatherId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCountyName() {
        return countyName;
    }

    public void setCountyName(String countyName) {
        this.countyName = countyName;
    }

    public int getCountyCode() {
        return countyCode;
    }

    public void setCountyCode(int countyCode) {
        this.countyCode = countyCode;
    }

    public int getCityId() {
        return cityId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }

    public int getWeatherId() {
        return weatherId;
    }

    public void setWeatherId(int weatherId) {
        this.weatherId = weatherId;
    }
}
