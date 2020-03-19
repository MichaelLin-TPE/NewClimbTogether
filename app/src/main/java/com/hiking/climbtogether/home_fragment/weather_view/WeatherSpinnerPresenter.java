package com.hiking.climbtogether.home_fragment.weather_view;

import java.util.ArrayList;

public interface WeatherSpinnerPresenter {

    void onShowRecyclerView();

    void onSpinnerItemSelectListener(String name, int position);

    void onShowCustomDialog(ArrayList<String> nationParkNameArray);
}
