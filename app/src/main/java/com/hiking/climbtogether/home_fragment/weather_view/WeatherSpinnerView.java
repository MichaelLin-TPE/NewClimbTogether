package com.hiking.climbtogether.home_fragment.weather_view;

import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hiking.climbtogether.R;


import java.util.ArrayList;

public class WeatherSpinnerView extends ConstraintLayout implements WeatherSpinnerVu {

    private Spinner spinner;

    private RecyclerView recyclerView;

    private Context context;

    private Handler handler;

    private ProgressBar progressBar;

    private WeatherSpinnerPresenter spinnerPresenter;

    private WeatherRecyclerViewAdapter adapter;


    public WeatherSpinnerView(Context context) {
        super(context);
        this.context = context;
        View view = View.inflate(context, R.layout.weather_spinner_layout, this);



        //在這邊要使用到 runOnUiThread所以才用這個
        handler = new Handler(context.getMainLooper());
        initPresenter();
        initView(view);

    }

    private void initPresenter() {
        spinnerPresenter = new WeatherSpinnerPresenterImpl(this);
    }

    private void initView(View view) {
        progressBar = view.findViewById(R.id.weather_progressbar);
        spinner = view.findViewById(R.id.weather_spinner);
        recyclerView = view.findViewById(R.id.weather_recycler_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(linearLayoutManager);
    }

    public void setData(ArrayList<String> nationParkNameArray) {

        spinnerPresenter.onShowSpinner(nationParkNameArray);

        //先採用舊的方法取得天氣

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int itemPosition, long l) {
                spinnerPresenter.onSpinnerItemSelectListener(itemPosition);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    //為了在mainThread做
    private void runOnUiThread(Runnable runnable) {
        handler.post(runnable);
    }


    @Override
    public void showSpinner(ArrayList<String> nationParkNameArray) {
        ArrayAdapter<String> stringArray = new ArrayAdapter<>(context, android.R.layout.simple_spinner_dropdown_item, nationParkNameArray);
        stringArray.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spinner.setAdapter(stringArray);

        spinnerPresenter.onShowRecyclerView();
    }

    @Override
    public Context getVuContext() {
        return context;
    }

    @Override
    public void showRecyclerView(ArrayList<String> weatherUrl, final ArrayList<String> timeArray, final ArrayList<String> tempArray, final ArrayList<String> rainArray, final ArrayList<String> imgUrlArray) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                adapter = new WeatherRecyclerViewAdapter(context);
                adapter.setData(timeArray,rainArray,tempArray,imgUrlArray);
                recyclerView.setAdapter(adapter);
            }
        });
    }

    @Override
    public void showProgressBar(final boolean isShow) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progressBar.setVisibility(isShow ? VISIBLE : GONE);
            }
        });

    }
}
