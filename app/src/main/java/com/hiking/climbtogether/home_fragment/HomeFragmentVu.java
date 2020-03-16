package com.hiking.climbtogether.home_fragment;

import android.content.Context;

import java.util.ArrayList;

public interface HomeFragmentVu {
    void showAnimationPhoto(ArrayList<String> photoUrlArray);

    Context getVuContext();

    void showWeatherSpinner(ArrayList<String> nationParkNameArray);
}
