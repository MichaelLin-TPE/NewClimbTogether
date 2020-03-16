package com.hiking.climbtogether.detail_activity.mt_presenter;

import com.hiking.climbtogether.db_modle.DataDTO;
import com.hiking.climbtogether.detail_activity.view.DetailViewHolder;
import com.hiking.climbtogether.detail_activity.view.PhotoViewHolder;
import com.hiking.climbtogether.detail_activity.view.WeatherViewHolder;

public interface MtPresenter {
    int getItemViewType(int position);

    int getItemCount();

    void setData(DataDTO data);

    void onBindPhotoViewHolder(PhotoViewHolder holder, int position);

    void onBindWeatherViewHolder(WeatherViewHolder holder, int position);

    void onBindDetailViewHolder(DetailViewHolder holder, int position);
}
