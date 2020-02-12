package com.example.climbtogether.login_activity;

import java.util.Map;

public class LoginActivityPresenterImpl implements LoginActivityPresenter {

    private LoginActivityVu mView;

    public LoginActivityPresenterImpl(LoginActivityVu mView){
        this.mView = mView;
    }

    @Override
    public void onLoginFail(String failMessage) {
        mView.showLoginFail(failMessage);
    }

    @Override
    public void onLoginSuccessful() {
        mView.closePage();
    }

    @Override
    public void onLoginButtonClickListener(String email, String password) {
        if (email == null || password == null || email.isEmpty() || password.isEmpty()){
            String message = "帳號或密碼不得為空值";
            mView.showLoginFail(message);
            return;
        }
        mView.signIn(email,password);
    }

    @Override
    public void onRegisterButtonClickListener() {
        mView.showRegisterDialog();
    }

    @Override
    public void onDialogRegisterButtonClickListener(String email,String password,String displayName,long currentTime) {
        if (email == null || email.isEmpty() || password == null || password.isEmpty() || displayName == null || displayName.isEmpty()){
            String message = "帳號,密碼或暱稱不得為空值";
            mView.showLoginFail(message);
            return;
        }

        mView.registerAccount(email,password,displayName,currentTime);

    }

    @Override
    public void onRegisterSuccessful() {
        String message = "註冊成功";
        mView.closePage();
        mView.showLoginFail(message);
    }

    @Override
    public void onGoogleSignInClickListener() {
        mView.signInWithGmail();
    }

    @Override
    public void onGoogleSignInSuccess() {
        mView.closePage();
    }

    @Override
    public void onCloudFirestoreRegisterEvent(Map<String, Object> data,String email) {
        mView.createFirestoreDocument(data,email);
    }

    @Override
    public void onViewChangeByGoogleSignIn() {
        mView.showAllButtonEnable(false);
    }
}
