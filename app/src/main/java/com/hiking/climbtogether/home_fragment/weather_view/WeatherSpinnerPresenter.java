package com.hiking.climbtogether.home_fragment.weather_view;

public interface WeatherSpinnerPresenter {

    void onGetWeatherData();

    void onTvSpinnerClickListener();

    void onLocationItemClickListener(String name,int itemPosition);
}
