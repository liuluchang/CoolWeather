package com.example.liuluchang.coolweather;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.liuluchang.coolweather.db.City;
import com.example.liuluchang.coolweather.db.County;
import com.example.liuluchang.coolweather.db.Province;
import com.example.liuluchang.coolweather.util.HttpUtil;
import com.example.liuluchang.coolweather.util.Utility;

import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Author: yx
 * Time:  2018/9/10
 * Description: This is ChooseAreaFragment
 */
public class ChooseAreaFragment extends android.support.v4.app.Fragment {

    private static final int LEVEL_PROVINCE = 0;

    private static final int LEVEL_CITY = 1;

    private static final int LEVEL_COUNTRY = 2;

    private ProgressDialog progressDialog;

    private TextView titleText;

    private Button backBtn;

    private ListView listView;

    private ArrayAdapter<String> adapter;

    private List<String> dataList = new ArrayList<>();

    //省列表
    private List<Province> provinceList;

    //城市列表
    private  List<City> cityList;

    //县列表
    private  List<County> countyList;

    //选中的省
    private Province selectedProvince;

    //选中的市
    private City selectedCity;

    //当前的选中的级别
    private int currentLevel;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.choose_area,container,false);

        //控件
        titleText = view.findViewById(R.id.title_text);
        backBtn = view.findViewById(R.id.back_button);
        listView = view.findViewById(R.id.area_list_view);

        //数据
        adapter = new ArrayAdapter<String>(getContext(),android.R.layout.simple_list_item_1,dataList);
        listView.setAdapter(adapter);

        return view;

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                if (currentLevel == LEVEL_PROVINCE){
                    selectedProvince = provinceList.get(i);
                    queryCities();
                }else if (currentLevel == LEVEL_CITY){
                    selectedCity = cityList.get(i);
                    queryCounties();
                }else if (currentLevel == LEVEL_COUNTRY){
                    String weatherId = countyList.get(i).getWeatherId();

                    Intent intent = new Intent(getActivity(),WeatherActivity.class);
                    intent.putExtra("weather_id",weatherId);
                    startActivity(intent);
                    getActivity().finish();

                }
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currentLevel == LEVEL_COUNTRY){
                    queryCities();
                }else  if (currentLevel == LEVEL_CITY){
                    queryProvinces();
                }
            }
        });

        //首先获取省级数据
        queryProvinces();

    }


    //获取省数据 优先查询数据库-》网络请求
    private void queryProvinces(){
        //设置导航栏
        titleText.setText("中国");
        backBtn.setVisibility(View.GONE);

        //数据库查询数据、接口获取数据
        provinceList = DataSupport.findAll(Province.class);

        if (provinceList.size()>0){

            dataList.clear();
            for(Province province : provinceList){
                dataList.add(province.getProvinceName());
            }
            //更新界面
            adapter.notifyDataSetChanged();
            listView.setSelection(0);

            currentLevel = LEVEL_PROVINCE;

        }else{
            //请求接口，保存数据
            String address = "http://guolin.tech/api/china";
            queryDataFromServer(address,"province");
        }

    }

    //获取市级数据 优先查询数据库-》网络请求
    private void queryCities(){

        //设置导航栏
        titleText.setText(selectedProvince.getProvinceName());
        backBtn.setVisibility(View.VISIBLE);

        //数据库查询数据、接口获取数据
        cityList = DataSupport.where("provinceid = ?", String.valueOf(selectedProvince.getId())).find(City.class);

        if (cityList.size() > 0){
            dataList.clear();
            for(City city : cityList){
                dataList.add(city.getCityName());
            }

            //更新界面
            adapter.notifyDataSetChanged();
            listView.setSelection(0);

            currentLevel = LEVEL_CITY;

        }else {
            //请求接口，保存数据
            int provinceCode = selectedProvince.getProvinceCode();
            String address = "http://guolin.tech/api/china/"+provinceCode;
            queryDataFromServer(address,"city");
        }

    }

    //获取县级数据 优先查询数据库-》网络请求
    private void queryCounties(){
        //设置导航栏
        titleText.setText(selectedCity.getCityName());
        backBtn.setVisibility(View.VISIBLE);

        //数据库查询数据、接口获取数据
        countyList = DataSupport.where("cityId = ?", String.valueOf(selectedCity.getId())).find(County.class);

        if (countyList.size() > 0){
            dataList.clear();
            for(County county : countyList){
                dataList.add(county.getCountyName());
            }

            //更新界面
            adapter.notifyDataSetChanged();
            listView.setSelection(0);

            currentLevel = LEVEL_COUNTRY;

        }else {
            //请求接口，保存数据
            int provinceCode = selectedProvince.getProvinceCode();
            int cityCode = selectedCity.getCityCode();
            String address = "http://guolin.tech/api/china/"+provinceCode+"/"+cityCode;
            queryDataFromServer(address,"country");
        }
    }

    //根据传入的地址和类型从服务器上查询省市县数据
    private void queryDataFromServer(String url , final String type){
        //转圈-主线程中执行
        showProgressDialog();

        HttpUtil.sendOkHttpRequest(url, new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        closeProgressDialog();
                        Toast.makeText(getContext(), "加载失败", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                //解析json
                String resposeText = response.body().string();
                boolean result = false;

                //存储到缓存数组、本地数据库
                if (type .equals("province")){
                    result = Utility.handleProvinceResponse(resposeText);
                }else if (type .equals("city")){
                    result = Utility.handleCityResponse(resposeText,selectedProvince.getId());
                }else if (type .equals("country")){
                    result = Utility.handleCountyResponse(resposeText,selectedCity.getId());
                }

                //更新界面
                if (result){
                    //UI操作在主线程中执行
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //转圈消失
                            closeProgressDialog();

                            //布局
                            if (type .equals("province")){
                                queryProvinces();
                            }else if (type .equals("city")){
                                queryCities();
                            }else if (type .equals("country")){
                                queryCounties();
                            }
                        }
                    });

                }

            }

        });


    }

    //提示框
    private void showProgressDialog(){
        if (progressDialog == null){
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("正在加载数据");
            progressDialog.setCanceledOnTouchOutside(false);

        }
        progressDialog.show();

    }

    private void closeProgressDialog(){

        if (progressDialog != null){
            progressDialog.dismiss();
        }
    }

}
