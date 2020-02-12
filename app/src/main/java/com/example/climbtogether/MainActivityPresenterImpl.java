package com.example.climbtogether;

public class MainActivityPresenterImpl implements MainActivityPresenter {

    private MainActivityVu mView;

    public MainActivityPresenterImpl(MainActivityVu mView){
        this.mView = mView;
    }

    @Override
    public void onBtnTestClickListener() {
        mView.intentToHomePageActivity();
    }
}
