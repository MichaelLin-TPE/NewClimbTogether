package com.example.climbtogether.personal_fragment;

import android.util.Log;

import java.util.ArrayList;

public class PersonalFragmentPresenterImpl implements PersonalFragmentPresenter {

    private PersonalFragmentVu mView;

    public PersonalFragmentPresenterImpl(PersonalFragmentVu mView) {
        this.mView = mView;
    }

    @Override
    public void onNoUserEvent() {
        mView.showLoginInformation(true);
    }

    @Override
    public void onLoginClickListener() {
        mView.intentToLoginActivity();
    }

    @Override
    public void onSearchUserChatData(String currentEmail, ArrayList<String> friendsArrayList) {
        mView.searchForChatData(currentEmail,friendsArrayList);
    }

    @Override
    public void onShowProgress(boolean isShow) {
        mView.showProgress(isShow);
    }

    @Override
    public void onCatchAllDataSucessful(ArrayList<PersonalChatDTO> chatDataArrayList) {
        mView.showProgress(false);
        Log.i("Micahel","資料長度 : "+chatDataArrayList.size());
        mView.setRecyclerView(chatDataArrayList);
    }

    @Override
    public void onItemClickListener(String displayName, String friendEmail, String photoUrl) {
        mView.intentToPersonalChatActivity(displayName,friendEmail,photoUrl);
    }
}
