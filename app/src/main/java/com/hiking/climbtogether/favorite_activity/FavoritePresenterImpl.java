package com.hiking.climbtogether.favorite_activity;

import com.google.gson.Gson;
import com.hiking.climbtogether.detail_activity.MountainObject;

public class FavoritePresenterImpl implements FavoritePresenter {

    private FavoriteActivityVu mView;

    private Gson gson;

    public FavoritePresenterImpl(FavoriteActivityVu mView) {
        this.mView = mView;
        gson = new Gson();
    }

    @Override
    public void onBackButtonClickListener() {
        mView.closePage();
    }

    @Override
    public void onCatchJson(String json) {
        if (json != null){
            MountainObject object = gson.fromJson(json,MountainObject.class);
            mView.setRecyclerView(object.getDataArrayList());
        }else {

        }


    }

    @Override
    public void onNotLoginEvent() {
        mView.intentToLoginPage();
    }
}
