package com.example.climbtogether.home_fragment.news_view;

import android.util.Log;

import com.example.climbtogether.tool.NationalParkWeatherAndNewsManager;

import java.util.ArrayList;

public class MountainNewsPresenterImpl implements MountainNewsPresenter {
    private MountainNewsVu mView;

    private NationalParkWeatherAndNewsManager newsManager;

    public MountainNewsPresenterImpl(MountainNewsVu mView){
        this.mView = mView;
        newsManager = new NationalParkWeatherAndNewsManager();
    }

    @Override
    public void onPrepareData() {

        mView.showProgressBar(true);

        newsManager.getNewsData(new NationalParkWeatherAndNewsManager.OnNewsListener() {
            @Override
            public void onSuccess(ArrayList<String> titleArrayList, ArrayList<String> locationArrayList, ArrayList<String> timeArrayList, ArrayList<String> newsUrlArrayList) {
                mView.showRecyclerView(titleArrayList,locationArrayList,timeArrayList,newsUrlArrayList);
                Log.i("Michael","取得資料 : "+timeArrayList.get(0));
                mView.showProgressBar(false);
            }

            @Override
            public void onFail(String message) {

            }
        });
    }

    @Override
    public void onItemClickListener(String url) {
        mView.intentToBrowser(url);
    }

}
