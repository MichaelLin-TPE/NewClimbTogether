package com.example.climbtogether.detail_activity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.climbtogether.R;
import com.example.climbtogether.detail_activity.mt_presenter.MtPresenter;
import com.example.climbtogether.detail_activity.view.DetailViewHolder;
import com.example.climbtogether.detail_activity.view.PhotoViewHolder;
import com.example.climbtogether.detail_activity.view.WeatherViewHolder;
import com.example.climbtogether.share_activity.ShareAdapter;

import static com.example.climbtogether.detail_activity.mt_presenter.MtPresenterImpl.INTRODUCE;
import static com.example.climbtogether.detail_activity.mt_presenter.MtPresenterImpl.PHOTO;
import static com.example.climbtogether.detail_activity.mt_presenter.MtPresenterImpl.WEATHER;

public class DetailAdapter extends RecyclerView.Adapter {

    private MtPresenter presenter;

    private Context context;

    public DetailAdapter(MtPresenter presenter, Context context) {
        this.presenter = presenter;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType){
            case PHOTO:
                return new PhotoViewHolder(LayoutInflater.from(context).inflate(R.layout.detail_item_photo,parent,false));
            case WEATHER:
                return new WeatherViewHolder(LayoutInflater.from(context).inflate(R.layout.detail_weather_view,parent,false),context);
            case INTRODUCE:
                return new DetailViewHolder(LayoutInflater.from(context).inflate(R.layout.detail_content_view,parent,false));
                default:
                    return null;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof PhotoViewHolder){
            presenter.onBindPhotoViewHolder((PhotoViewHolder)holder,position);
        }
        if (holder instanceof WeatherViewHolder){
            presenter.onBindWeatherViewHolder((WeatherViewHolder)holder,position);
        }
        if (holder instanceof  DetailViewHolder){
            presenter.onBindDetailViewHolder((DetailViewHolder)holder,position);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return presenter.getItemViewType(position);
    }

    @Override
    public int getItemCount() {
        return presenter.getItemCount();
    }
}
