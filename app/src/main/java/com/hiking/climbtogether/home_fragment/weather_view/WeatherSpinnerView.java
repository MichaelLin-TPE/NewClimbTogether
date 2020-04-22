package com.hiking.climbtogether.home_fragment.weather_view;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hiking.climbtogether.R;
import com.hiking.climbtogether.home_fragment.json_object.WeatherElement;


import java.util.ArrayList;

public class WeatherSpinnerView extends ConstraintLayout implements WeatherSpinnerVu {

    private TextView tvSpinner;

    private RecyclerView recyclerView;

    private Context context;

    private Handler handler;

    private WeatherSpinnerPresenter spinnerPresenter;

    private WeatherRecyclerViewAdapter adapter;


    private ProgressBar progressBar;

    private LocationAdapter locationAdapter;

    private AlertDialog locationDialog;

    private int userPressIndex;


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
        tvSpinner = view.findViewById(R.id.weather_spinner);
        recyclerView = view.findViewById(R.id.weather_recycler_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(linearLayoutManager);
    }




    public void showWeatherView() {
        spinnerPresenter.onGetWeatherData();
    }
    @Override
    public void setSpinnerText(String location) {
        tvSpinner.setText(location);

        tvSpinner.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                spinnerPresenter.onTvSpinnerClickListener();
            }
        });
    }

    @Override
    public void showLocationListDialog(ArrayList<String> locationArray) {
        View view = View.inflate(getContext(),R.layout.location_list_dialog,null);
        RecyclerView rvLocation = view.findViewById(R.id.location_list_recycler_view);

        rvLocation.setLayoutManager(new GridLayoutManager(getContext(),4));

        locationAdapter = new LocationAdapter(locationArray,getContext());
        locationAdapter.setUserPressIndex(userPressIndex);
        rvLocation.setAdapter(locationAdapter);

        locationDialog = new AlertDialog.Builder(getContext())
                .setView(view).create();
        locationDialog.show();
        locationAdapter.setOnLocationItemClickListener(new LocationAdapter.OnLocationItemClickListener() {
            @Override
            public void onClick(String name,int itemPosition) {
                Log.i("Michael","點擊了 : "+name);
                spinnerPresenter.onLocationItemClickListener(name,itemPosition);
            }
        });


    }

    @Override
    public void changeView(int itemPosition) {
        userPressIndex = itemPosition;
        locationAdapter.setUserPressIndex(userPressIndex);
        locationAdapter.notifyDataSetChanged();
        locationDialog.dismiss();
    }

    @Override
    public void setRecyclerView(ArrayList<WeatherElement> dataArray) {
        adapter = new WeatherRecyclerViewAdapter(getContext());
        adapter.setData(dataArray);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void showProgress(boolean isShow) {
        progressBar.setVisibility(isShow ? VISIBLE : GONE);
    }

    @Override
    public void changeRecyclerView(ArrayList<WeatherElement> dataArray) {
        adapter.setData(dataArray);
        adapter.notifyDataSetChanged();
    }
}
