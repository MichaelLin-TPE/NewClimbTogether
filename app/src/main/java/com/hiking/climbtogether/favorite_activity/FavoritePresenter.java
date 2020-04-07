package com.hiking.climbtogether.favorite_activity;

public interface FavoritePresenter {
    void onBackButtonClickListener();

    void onCatchJson(String json);

    void onNotLoginEvent();
}
