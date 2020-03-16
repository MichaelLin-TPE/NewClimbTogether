package com.hiking.climbtogether.login_activity;

import java.util.Map;

public interface LoginActivityPresenter {
    void onLoginFail(String failMessage);

    void onLoginSuccessful();

    void onLoginButtonClickListener(String email, String password);

    void onRegisterButtonClickListener();

    void onDialogRegisterButtonClickListener(String email,String password,String displayName,long currentTime);

    void onRegisterSuccessful();

    void onGoogleSignInClickListener();

    void onGoogleSignInSuccess();

    void onCloudFirestoreRegisterEvent(Map<String, Object> data,String email);

    void onViewChangeByGoogleSignIn();
}
