package com.hiking.climbtogether.home_fragment.view_presenter;

import com.hiking.climbtogether.home_fragment.anmiation_photo_view.AnimationPhotoViewHolder;
import com.hiking.climbtogether.home_fragment.news_view.MountNewsViewHolder;
import com.hiking.climbtogether.home_fragment.weather_view.WeatherSpinnerViewHolder;

import java.util.ArrayList;

public interface ViewPresenter {

    void onSetAnimationPhotoData(ArrayList<String> photoUrlArray);

    int getItemCount();

    int getItemViweType(int position);

    void onBindAnimationViewHolder(AnimationPhotoViewHolder holder, int position);

    void onSetSpinnerData(ArrayList<String> nationParkNameArray);

    void onBindWeatherSPinnerViewHolder(WeatherSpinnerViewHolder holder, int position);

    void onBindMountainNewsViewHolder(MountNewsViewHolder holder, int position);

    void onShowNewsData();
}
