package com.hiking.climbtogether.home_fragment.view_presenter;

import com.hiking.climbtogether.home_fragment.anmiation_photo_view.AnimationPhotoViewHolder;
import com.hiking.climbtogether.home_fragment.news_view.MountNewsViewHolder;
import com.hiking.climbtogether.home_fragment.weather_view.WeatherSpinnerViewHolder;

import java.util.ArrayList;

public class ViewPresenterImpl implements ViewPresenter {

    public static final int ANIMATION_PIC = 0;

    public static final int WEATHER_SPINNER = 1;

    public static final int NEWS = 2;

    private ArrayList<String> photoUrlArray;

    private ArrayList<String> nationParkNameArray;

    private boolean isAnimationShow;

    private boolean isSpinnerShow;

    private boolean isNewsShow;

    @Override
    public void onSetAnimationPhotoData(ArrayList<String> photoUrlArray) {
        this.photoUrlArray = photoUrlArray;
        isAnimationShow = true;
    }

    @Override
    public int getItemCount() {
        int itemCount = 0;
        if (isAnimationShow){
            itemCount ++;
        }
        if (isSpinnerShow){
            itemCount ++;
        }
        if (isNewsShow){
            itemCount ++;
        }
        return itemCount;
    }

    @Override
    public int getItemViweType(int position) {
        if(position == 0 && isAnimationShow){
            return ANIMATION_PIC;
        }
        //天氣先暫時不用 再想想要做甚麼
        if (position == 1 && isSpinnerShow){
            return WEATHER_SPINNER;
        }
        if (position == 2 && isNewsShow){
            return NEWS;
        }
        return 0;
    }

    @Override
    public void onBindAnimationViewHolder(AnimationPhotoViewHolder holder, int position) {
        holder.setPhotoUrlData(photoUrlArray);
    }

    @Override
    public void onSetSpinnerData(ArrayList<String> nationParkNameArray) {
        this.nationParkNameArray = nationParkNameArray;
        isSpinnerShow = true;
    }

    @Override
    public void onBindWeatherSPinnerViewHolder(WeatherSpinnerViewHolder holder, int position) {

        holder.setNationalNameData(nationParkNameArray);
    }

    @Override
    public void onBindMountainNewsViewHolder(MountNewsViewHolder holder, int position) {
        holder.showNews();
    }

    @Override
    public void onShowNewsData() {
        isNewsShow = true;
    }
}
