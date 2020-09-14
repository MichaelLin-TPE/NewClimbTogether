package com.hiking.climbtogether;

public interface MainActivityPresenter {

    void onApplyToken();

    void onSaveCurrentUserData();

    void onUpdateUserToken(String token);

    void onIntentToHomeActivity();
}
