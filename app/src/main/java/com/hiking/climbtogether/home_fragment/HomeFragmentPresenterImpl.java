package com.hiking.climbtogether.home_fragment;

import com.hiking.climbtogether.R;

import java.util.ArrayList;

public class HomeFragmentPresenterImpl implements HomeFragmentPresenter {

    private HomeFragmentVu mView;

    public HomeFragmentPresenterImpl(HomeFragmentVu mView){
        this.mView = mView;
    }

    @Override
    public void onPrepareData() {
        ArrayList<String> photoUrlArray = new ArrayList<>();

        photoUrlArray.add("https://i.imgur.com/qPLwdHq.jpg");
        photoUrlArray.add("https://i.imgur.com/KiC5b0u.jpg");
        photoUrlArray.add("https://i.imgur.com/kHhRdA5.jpg");
        photoUrlArray.add("https://i.imgur.com/r6Nok6c.jpg");
        photoUrlArray.add("https://i.imgur.com/XwHEKrs.jpg");

        mView.showAnimationPhoto(photoUrlArray);
    }

    @Override
    public void onPrepareSpinnerData() {
        ArrayList<String> nationParkNameArray = new ArrayList<>();;
        nationParkNameArray.add(mView.getVuContext().getResources().getString(R.string.yu_shan_national_park));
        nationParkNameArray.add(mView.getVuContext().getResources().getString(R.string.snow_mt_national_park));
        nationParkNameArray.add(mView.getVuContext().getResources().getString(R.string.taruku_national_park));
        nationParkNameArray.add(mView.getVuContext().getResources().getString(R.string.young_ming_national_par));

        mView.showWeatherSpinner(nationParkNameArray);
    }
}
