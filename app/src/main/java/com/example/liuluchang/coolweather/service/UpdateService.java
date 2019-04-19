package com.example.liuluchang.coolweather.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.os.SystemClock;
import android.preference.PreferenceManager;

import com.example.liuluchang.coolweather.gson.Basic;
import com.example.liuluchang.coolweather.gson.Weather;
import com.example.liuluchang.coolweather.util.HttpUtil;
import com.example.liuluchang.coolweather.util.Utility;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class UpdateService extends Service {
    public UpdateService() {

    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        //更新
        updateWeatherInfo();
        updateBingPic();

        //初始化定时器,后台定时运行的任务
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        int msecond = 8*60*60*1000;
        long triggerTime = SystemClock.elapsedRealtime()+msecond;
        Intent intent1 = new Intent(this,UpdateService.class);
        PendingIntent pendingIntent = PendingIntent.getService(this,0,intent1,0);
        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,triggerTime,pendingIntent);

        return super.onStartCommand(intent, flags, startId);
    }

    private void updateBingPic() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String weatherString = preferences.getString("weather",null);
        if (weatherString != null){
            Weather weather = Utility.handleWeatherResponse(weatherString);

            String weatherUrl = "http://guolin.tech/api/weather?cityid=" + weather.basic.weatherId + "&key=bc0418b57b2d4918819d3974ac1285d9";
            HttpUtil.sendOkHttpRequest(weatherUrl, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    e.printStackTrace();
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String responseString = response.body().string();

                    SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(UpdateService.this).edit();
                    editor.putString("weather",responseString);
                    editor.apply();
                }
            });
        }
    }

    private void updateWeatherInfo() {
        String requestBingPic = "http://guolin.tech/api/bing_pic";
        HttpUtil.sendOkHttpRequest(requestBingPic, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String pic_Url = response.body().string();

                SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(UpdateService.this).edit();
                editor.putString("bingPic",pic_Url);
                editor.apply();
            }
        });
    }

}
