package com.example.climbtogether.home_fragment.weather_view;

import java.util.ArrayList;

public interface WeatherSpinnerPresenter {
    void onShowSpinner(ArrayList<String> nationParkNameArray);

    void onShowRecyclerView();

    void onSpinnerItemSelectListener(int itemPosition);
}
