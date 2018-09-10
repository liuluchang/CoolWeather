package com.example.liuluchang.coolweather;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: yx
 * Time:  2018/9/10
 * Description: This is ChooseAreaFragment
 */
public class ChooseAreaFragment extends Fragment {

    private ProgressDialog progressDialog;

    private TextureView titleText;

    private Button backBtn;

    private ListView listView;

    private ArrayAdapter<String> adapter;

    private List<String> dataList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {


        return super.onCreateView(inflater, container, savedInstanceState);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }
}
