package com.example.liuluchang.coolweather;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.liuluchang.coolweather.gson.Weather;
import com.example.liuluchang.coolweather.util.HttpUtil;
import com.example.liuluchang.coolweather.util.Utility;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class WeatherActivity extends AppCompatActivity {

    private ScrollView weatherLayout;

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


        //获取数据
        mWeatherId = getIntent().getStringExtra("weather_id");
        weatherLayout.setVisibility(View.VISIBLE);
        requestWeather(mWeatherId);
    }

    //请求接口获取天气详情数据
    public void requestWeather(String weatherId){
        String weatherUrl = "http://guolin.tech/api/weather?cityid=" + weatherId + "&key=bc0418b57b2d4918819d3974ac1285d9";
        HttpUtil.sendOkHttpRequest(weatherUrl, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseText = response.body().string();
                Weather weather = Utility.handleWeatherResponse(responseText);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                    }
                });
            }
        });

    }
}
