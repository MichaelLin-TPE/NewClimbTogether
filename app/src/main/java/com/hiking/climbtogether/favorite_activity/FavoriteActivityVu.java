package com.hiking.climbtogether.favorite_activity;

import com.hiking.climbtogether.detail_activity.MountainFavoriteData;

import java.util.ArrayList;

public interface FavoriteActivityVu {
    void closePage();

    void setRecyclerView(ArrayList<MountainFavoriteData> dataArrayList);

    void intentToLoginPage();

    void showNoDataView(boolean isShow);
}
