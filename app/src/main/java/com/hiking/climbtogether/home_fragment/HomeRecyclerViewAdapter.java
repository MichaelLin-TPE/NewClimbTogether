package com.hiking.climbtogether.home_fragment;

import android.content.Context;
import android.util.Log;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hiking.climbtogether.home_fragment.anmiation_photo_view.AnimationPhotoView;
import com.hiking.climbtogether.home_fragment.anmiation_photo_view.AnimationPhotoViewHolder;
import com.hiking.climbtogether.home_fragment.news_view.MountNewsViewHolder;
import com.hiking.climbtogether.home_fragment.news_view.MountainNewsView;
import com.hiking.climbtogether.home_fragment.weather_view.WeatherSpinnerView;
import com.hiking.climbtogether.home_fragment.weather_view.WeatherSpinnerViewHolder;
import com.hiking.climbtogether.home_fragment.view_presenter.ViewPresenter;

import static com.hiking.climbtogether.home_fragment.view_presenter.ViewPresenterImpl.ANIMATION_PIC;
import static com.hiking.climbtogether.home_fragment.view_presenter.ViewPresenterImpl.NEWS;
import static com.hiking.climbtogether.home_fragment.view_presenter.ViewPresenterImpl.WEATHER_SPINNER;

public class HomeRecyclerViewAdapter extends RecyclerView.Adapter {

    private Context context;

    private ViewPresenter viewPresenter;

    public HomeRecyclerViewAdapter(Context context,ViewPresenter viewPresenter){
        this.context = context;
        this.viewPresenter = viewPresenter;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        switch (viewType){
            case ANIMATION_PIC:
                AnimationPhotoView photoView = new AnimationPhotoView(context);
                AnimationPhotoViewHolder photoViewHolder = new AnimationPhotoViewHolder(photoView);
                return photoViewHolder;
            case WEATHER_SPINNER:
                WeatherSpinnerView spinnerView = new WeatherSpinnerView(context);
                WeatherSpinnerViewHolder spinnerViewHolder = new WeatherSpinnerViewHolder(spinnerView);
                return spinnerViewHolder;
            case NEWS:
                Log.i("Michael","有執行到News");
                MountainNewsView newsView = new MountainNewsView(context);
                MountNewsViewHolder newsViewHolder = new MountNewsViewHolder(newsView);
                return newsViewHolder;
            default:
                return null;
        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof AnimationPhotoViewHolder){
            viewPresenter.onBindAnimationViewHolder((AnimationPhotoViewHolder)holder,position);
        }if (holder instanceof WeatherSpinnerViewHolder){
            viewPresenter.onBindWeatherSPinnerViewHolder((WeatherSpinnerViewHolder)holder,position);
        }if (holder instanceof MountNewsViewHolder){
            Log.i("Michael","有執行到onNewsBindViewHolder");
            viewPresenter.onBindMountainNewsViewHolder((MountNewsViewHolder)holder,position);
        }
    }

    @Override
    public int getItemCount() {
        return viewPresenter.getItemCount();
    }

    @Override
    public int getItemViewType(int position) {
        return viewPresenter.getItemViweType(position);
    }
}
