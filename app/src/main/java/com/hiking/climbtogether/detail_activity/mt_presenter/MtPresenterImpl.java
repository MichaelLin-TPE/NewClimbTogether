package com.hiking.climbtogether.detail_activity.mt_presenter;

import com.hiking.climbtogether.db_modle.DataDTO;
import com.hiking.climbtogether.detail_activity.view.DetailViewHolder;
import com.hiking.climbtogether.detail_activity.view.PhotoViewHolder;
import com.hiking.climbtogether.detail_activity.view.WeatherViewHolder;

public class MtPresenterImpl implements MtPresenter {

    public static final int PHOTO = 0;
    public static final int WEATHER = 1;
    public static final int INTRODUCE = 2;

    private DataDTO data;

    @Override
    public int getItemViewType(int position) {
        if (position == 0){
            return PHOTO;
        }
//        if (position == 1){
//            return WEATHER;
//        }
        if (position == 1){
            return INTRODUCE;
        }
        return 0;
    }

    @Override
    public int getItemCount() {
        return 2;
    }

    @Override
    public void setData(DataDTO data) {
        this.data = data;
    }

    @Override
    public void onBindPhotoViewHolder(PhotoViewHolder holder, int position) {
        holder.setData(data.getphoto());
    }

    @Override
    public void onBindWeatherViewHolder(WeatherViewHolder holder, int position) {
        holder.setData(data.getWeatherUrl());
    }

    @Override
    public void onBindDetailViewHolder(DetailViewHolder holder, int position) {
        holder.setData(data);
    }
}
