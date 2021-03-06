package com.example.liuluchang.coolweather.util;

import android.text.TextUtils;

import com.example.liuluchang.coolweather.db.City;
import com.example.liuluchang.coolweather.db.County;
import com.example.liuluchang.coolweather.db.Province;
import com.example.liuluchang.coolweather.gson.Weather;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Author: liuluchang
 * Time:  2018/9/10
 * Description: This is Utility
 */

public class Utility {

    //解析和处理服务器返回的省级数据
    public static boolean handleProvinceResponse(String responseString){
        if (!TextUtils.isEmpty(responseString)){
            try {
            	//TODO
                JSONArray allProvince = new JSONArray(responseString);
                for (int i = 0; i < allProvince.length(); i++){
                    JSONObject provinceObject = allProvince.getJSONObject(i);
                    Province province = new Province();
                    province.setProvinceName(provinceObject.getString("name"));
                    province.setProvinceCode(provinceObject.getInt("id"));
                    province.save();
                }

                return  true;
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        return false;
    }

    //解析和处理服务器返回的市级数据
    public static boolean handleCityResponse(String response, int provinceId){
        if (!TextUtils.isEmpty(response)){
            try {
            	//TODO
                JSONArray allCity = new JSONArray(response);
                for (int i = 0; i < allCity.length(); i++){
                    JSONObject cityObject = allCity.getJSONObject(i);
                    City city = new City();
                    city.setCityName(cityObject.getString("name"));
                    city.setCityCode(cityObject.getInt("id"));
                    city.setProvinceId(provinceId);
                    city.save();

                }

                return true;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return false;
    }

    //解析和处理服务器返回的县级数据
    public static boolean handleCountyResponse(String response, int cityId){
        if (!TextUtils.isEmpty(response)){
            try {
            	//TODO
                JSONArray allCountry = new JSONArray(response);
                for (int i = 0; i < allCountry.length(); i++){
                    JSONObject countryObject = allCountry.getJSONObject(i);
                    County county = new County();
                    county.setCountyName(countryObject.getString("name"));
                    county.setWeatherId(countryObject.getString("weather_id"));
                    county.setCountyCode(countryObject.getInt("id"));
                    county.setCityId(cityId);
                    county.save();

                }

                return true;
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        return false;
    }

    //将返回的JSON数据解析成Weather实体类
    public static Weather handleWeatherResponse(String response){
        try {
        	//TODO
            JSONObject jsonObject = new JSONObject(response);
            JSONArray jsonArray = jsonObject.getJSONArray("HeWeather");
            String weatherContent = jsonArray.get(0).toString();

            Weather weather = new Gson().fromJson(weatherContent,Weather.class);
            return weather;

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

}
