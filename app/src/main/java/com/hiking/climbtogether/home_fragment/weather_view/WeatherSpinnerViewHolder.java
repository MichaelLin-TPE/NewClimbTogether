package com.hiking.climbtogether.home_fragment.weather_view;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class WeatherSpinnerViewHolder extends RecyclerView.ViewHolder {

    private WeatherSpinnerView weatherSpinnerView;

    public WeatherSpinnerViewHolder(@NonNull View itemView) {
        super(itemView);
        weatherSpinnerView = (WeatherSpinnerView)itemView;
    }

    public void setNationalNameData(ArrayList<String> nationParkNameArray) {
        weatherSpinnerView.showWeatherView();
    }
}
