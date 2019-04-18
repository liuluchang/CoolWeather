package com.example.liuluchang.coolweather;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.liuluchang.coolweather.gson.Forecast;
import com.example.liuluchang.coolweather.gson.Weather;
import com.example.liuluchang.coolweather.util.HttpUtil;
import com.example.liuluchang.coolweather.util.Utility;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import okio.InflaterSource;

public class WeatherActivity extends AppCompatActivity {

    private ScrollView weatherLayout;

    public SwipeRefreshLayout swipeRefreshLayout;

    private Button navLeftBtn;

    private TextView titleCity;

    private TextView titleUpdateTime;

    private TextView degreeText;

    private TextView weatherInfoText;

    private LinearLayout forecastLayout;

    private TextView aqiText;

    private TextView pm25Text;

    private TextView comfortText;

    private TextView carWashText;

    private TextView sportText;

    private ImageView bingPicImg;

    private String mWeatherId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= 21){
            View decView = getWindow().getDecorView();
            decView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }

        setContentView(R.layout.activity_weather);

        //初始化控件
        weatherLayout = findViewById(R.id.weather_scroller);
        titleCity =  findViewById(R.id.title_city);
        titleUpdateTime =  findViewById(R.id.title_update_time);
        degreeText =  findViewById(R.id.degree_text);
        weatherInfoText =  findViewById(R.id.weather_info_text);
        forecastLayout =  findViewById(R.id.forecast_layout);
        aqiText =  findViewById(R.id.aqi_text);
        pm25Text =  findViewById(R.id.pm25_text);
        comfortText =  findViewById(R.id.comfort_text);
        carWashText =  findViewById(R.id.car_wash_text);
        sportText =  findViewById(R.id.sport_text);
        bingPicImg = findViewById(R.id.bing_pic_img);
        swipeRefreshLayout = findViewById(R.id.swipe_refresh);
        navLeftBtn = findViewById(R.id.nav_left_button);

        //获取数据
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String weatherString = preferences.getString("weather",null);
        if (weatherString != null ){
            //有缓存的情况
            Weather weather = Utility.handleWeatherResponse(weatherString);
            mWeatherId = weather.basic.weatherId;
            showWeatherInfos(weather);
        } else {
            //无缓存泽请求数据
            mWeatherId = getIntent().getStringExtra("weather_id");
            weatherLayout.setVisibility(View.INVISIBLE);
            requestWeather(mWeatherId);
        }


        String bingPic = preferences.getString("bingPic",null);
        if (bingPic != null){
            Glide.with(WeatherActivity.this).load(bingPic).into(bingPicImg);
        }else {
            //加载背景图片
            loadBingPic();
        }

        //swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                requestWeather(mWeatherId);
            }
        });

        //导航栏按钮
        navLeftBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.print("ssssss");
            }
        });
    }

    //请求背景图片
    private void loadBingPic(){
        String requestBingPic = "http://guolin.tech/api/bing_pic";
        HttpUtil.sendOkHttpRequest(requestBingPic, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
               final String bingPicString = response.body().string();

               runOnUiThread(new Runnable() {
                   @Override
                   public void run() {
                       SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(WeatherActivity.this).edit();
                       editor.putString("bingPic",bingPicString);
                       editor.apply();

                       Glide.with(WeatherActivity.this).load(bingPicString).into(bingPicImg);
                   }
               });
            }
        });

    }

    //请求接口获取天气详情数据
    public void requestWeather(String weatherId){
        String weatherUrl = "http://guolin.tech/api/weather?cityid=" + weatherId + "&key=bc0418b57b2d4918819d3974ac1285d9";
        HttpUtil.sendOkHttpRequest(weatherUrl, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(WeatherActivity.this, "获取天气数据失败!", Toast.LENGTH_SHORT).show();
                        swipeRefreshLayout.setRefreshing(false);
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
               final String responseString = response.body().string();
               final Weather weather = Utility.handleWeatherResponse(responseString);

               runOnUiThread(new Runnable() {
                   @Override
                   public void run() {

                       if (weather != null && "ok".equals(weather.status)){
                           //缓存数据,不需要都请求
                           SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(WeatherActivity.this).edit();
                           editor.putString("weather",responseString);
                           editor.apply();
                           //更新weatherID
                           mWeatherId = weather.basic.weatherId;
                           //展示数据
                           showWeatherInfos(weather);

                       } else {
                           Toast.makeText(WeatherActivity.this, "获取天气数据失败!", Toast.LENGTH_SHORT).show();

                       }

                       swipeRefreshLayout.setRefreshing(false);

                   }
               });
            }
        });
    }

    //展示数据
    private void showWeatherInfos(Weather weather){
        //头部
        String cityName = weather.basic.cityName;
        titleCity.setText(cityName);

        String updateTime = weather.basic.update.updateTime.split(" ")[1];
        titleUpdateTime.setText(updateTime);

        //当前信息
        String degree = weather.now.temperature+"℃";
        degreeText.setText(degree);

        String waetherInfo = weather.now.more.info;
        weatherInfoText.setText(waetherInfo);

        //天气预测
        forecastLayout.removeAllViews();
        for (Forecast forecast : weather.forecastList ){
            View view = LayoutInflater.from(this).inflate(R.layout.forecast_item,forecastLayout,false);

            TextView dateText = view.findViewById(R.id.date_text);
            TextView infoText = view.findViewById(R.id.info_text);
            TextView maxText = view.findViewById(R.id.max_text);
            TextView minText = view.findViewById(R.id.min_text);

            dateText.setText(forecast.date);
            infoText.setText(forecast.more.info);
            maxText.setText(forecast.temperature.max);
            minText.setText(forecast.temperature.min);
            forecastLayout.addView(view);
        }

        //空气质量
        aqiText.setText(weather.aqi.city.aqi);
        pm25Text.setText(weather.aqi.city.pm25);

        //建议
        comfortText.setText(weather.suggestion.comfort.info);
        carWashText.setText(weather.suggestion.carWash.info);
        sportText.setText(weather.suggestion.sport.info);

        //显示
        weatherLayout.setVisibility(View.VISIBLE);
    }


}
