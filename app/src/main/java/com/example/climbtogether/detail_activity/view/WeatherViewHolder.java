package com.example.climbtogether.detail_activity.view;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.climbtogether.R;
import com.example.climbtogether.home_fragment.weather_view.WeatherRecyclerViewAdapter;
import com.example.climbtogether.tool.NationalParkWeatherAndNewsManager;
import com.nostra13.universalimageloader.utils.L;

import java.util.ArrayList;

public class WeatherViewHolder extends RecyclerView.ViewHolder {


    private RecyclerView recyclerView;

    private NationalParkWeatherAndNewsManager weatherManager;

    private Handler handler;

    private Context context;

    public WeatherViewHolder(@NonNull View itemView, Context context) {
        super(itemView);
        recyclerView = itemView.findViewById(R.id.detail_weather_recycler_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        linearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        weatherManager = new NationalParkWeatherAndNewsManager();
        handler = new Handler(context.getMainLooper());
        this.context = context;
    }

    private void runOnUiThread(Runnable runnable){
        handler.post(runnable);
    }

    public void setData(String weatherUrl) {
        weatherManager.getDataFromHTML(weatherUrl, new NationalParkWeatherAndNewsManager.OnWeatherListener() {
            @Override
            public void onSuccess(ArrayList<String> timeArray, ArrayList<String> tempArray, ArrayList<String> rainArray, ArrayList<String> imgUrlArray) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        WeatherRecyclerViewAdapter adapter = new WeatherRecyclerViewAdapter(context);
                        adapter.setData(timeArray,rainArray,tempArray,imgUrlArray);
                        recyclerView.setAdapter(adapter);
                    }
                });
            }

            @Override
            public void onFail(String message) {
                Log.i("Michael","天氣錯誤 : "+message);
            }
        });
    }
}
