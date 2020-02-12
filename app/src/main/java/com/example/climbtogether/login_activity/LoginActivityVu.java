package com.example.climbtogether.login_activity;

import java.util.Map;

public interface LoginActivityVu {
    void showLoginFail(String failMessage);

    void closePage();

    void signIn(String email, String password);

    void showRegisterDialog();

    void registerAccount(String email, String password, String displayName, long currentTime);

    void signInWithGmail();

    void createFirestoreDocument(Map<String, Object> data,String email);

    void showAllButtonEnable(boolean isEnable);
}
