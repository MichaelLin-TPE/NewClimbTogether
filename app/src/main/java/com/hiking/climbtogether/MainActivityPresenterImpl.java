package com.hiking.climbtogether;

public class MainActivityPresenterImpl implements MainActivityPresenter {

    private MainActivityVu mView;

    public MainActivityPresenterImpl(MainActivityVu mView){
        this.mView = mView;
    }

    @Override
    public void onApplyToken() {
        mView.applyToken();
    }

    @Override
    public void onSaveCurrentUserData() {
        mView.saveCurrentUserData();
    }
}
