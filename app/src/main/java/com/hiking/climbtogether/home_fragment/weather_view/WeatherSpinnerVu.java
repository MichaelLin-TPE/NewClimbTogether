package com.hiking.climbtogether.home_fragment.weather_view;

import com.hiking.climbtogether.home_fragment.json_object.WeatherElement;

import java.util.ArrayList;

public interface WeatherSpinnerVu {


    void setSpinnerText(String location);

    void showLocationListDialog(ArrayList<String> locationArray);

    void changeView(int itemPosition);

    void setRecyclerView(ArrayList<WeatherElement> dataArray);

    void showProgress(boolean isShow);

    void changeRecyclerView(ArrayList<WeatherElement> dataArray);
}
