package com.example.liuluchang.coolweather.util;

import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * Author: liuluchang
 * Time:  2018/9/10
 * Description: This is HttpUtil
 */

public class HttpUtil {

    public static void sendOkHttpRequest(String address,okhttp3.Callback callback){

        Request request = new Request.Builder()
                .url(address)
                .build();

        OkHttpClient client = new OkHttpClient();
        client.newCall(request).enqueue(callback);

    }

}
