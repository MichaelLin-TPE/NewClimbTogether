package com.hiking.climbtogether.home_fragment.weather_view;

import android.util.Log;

import com.hiking.climbtogether.tool.NationalParkWeatherAndNewsManager;

import java.util.ArrayList;

public class WeatherSpinnerPresenterImpl implements WeatherSpinnerPresenter {

    private WeatherSpinnerVu mView;

    private NationalParkWeatherAndNewsManager weatherManager;

    private ArrayList<String> weatherUrl;

    public WeatherSpinnerPresenterImpl(WeatherSpinnerVu mView){
        this.mView = mView;
        weatherManager = new NationalParkWeatherAndNewsManager();
    }

    @Override
    public void onShowRecyclerView() {
        weatherUrl = new ArrayList<>();
        /**
         * 0 = 太魯閣
         * 1 = 玉山
         * 2 = 陽明山
         * 3 = 雪山
         */
        weatherUrl.add("https://www.cwb.gov.tw/V7/forecast/entertainment/7Day/E013.htm");
        weatherUrl.add("https://www.cwb.gov.tw/V7/forecast/entertainment/7Day/E020.htm");
        weatherUrl.add("https://www.cwb.gov.tw/V7/forecast/entertainment/7Day/E004.htm");
        weatherUrl.add("https://www.cwb.gov.tw/V7/forecast/entertainment/7Day/E028.htm");

        mView.showProgressBar(true);

        weatherManager.getDataFromHTML(weatherUrl.get(0), new NationalParkWeatherAndNewsManager.OnWeatherListener() {
            @Override
            public void onSuccess(final ArrayList<String> timeArray, final ArrayList<String> tempArray, final ArrayList<String> rainArray, final ArrayList<String> imgUrlArray) {

                Log.i("Michael", "日期 : " + timeArray.get(0)+"資料長度 : "+timeArray.size());

               mView.showRecyclerView(weatherUrl,timeArray,tempArray,rainArray,imgUrlArray);

               mView.showProgressBar(false);
            }

            @Override
            public void onFail(String message) {

            }
        });
    }

    @Override
    public void onSpinnerItemSelectListener(String name, int position) {
        weatherManager.getDataFromHTML(weatherUrl.get(position), new NationalParkWeatherAndNewsManager.OnWeatherListener() {
            @Override
            public void onSuccess(ArrayList<String> timeArray, ArrayList<String> tempArray, ArrayList<String> rainArray, ArrayList<String> imgUrlArray) {
                Log.i("Michael", "日期 : " + timeArray.get(0)+"資料長度 : "+timeArray.size());
                mView.showRecyclerView(weatherUrl,timeArray,tempArray,rainArray,imgUrlArray);
            }

            @Override
            public void onFail(String message) {

            }
        });
    }

    @Override
    public void onShowCustomDialog(ArrayList<String> nationParkNameArray) {
        mView.showDialog(nationParkNameArray);
    }
}
