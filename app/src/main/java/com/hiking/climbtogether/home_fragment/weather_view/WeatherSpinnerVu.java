package com.hiking.climbtogether.home_fragment.weather_view;

import android.content.Context;

import java.util.ArrayList;

public interface WeatherSpinnerVu {

    Context getVuContext();

    void showRecyclerView(ArrayList<String> weatherUrl,ArrayList<String> timeArray, ArrayList<String> tempArray, ArrayList<String> rainArray, ArrayList<String> imgUrlArray);

    void showProgressBar(boolean isShow);

    void showDialog(ArrayList<String> nationParkNameArray);
}
