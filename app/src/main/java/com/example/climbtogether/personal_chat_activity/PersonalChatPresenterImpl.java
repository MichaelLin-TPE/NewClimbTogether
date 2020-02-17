package com.example.climbtogether.personal_chat_activity;

import java.util.ArrayList;

public class PersonalChatPresenterImpl implements PersonalChatPresenter {

    private PersonalChatVu mView;

    public PersonalChatPresenterImpl(PersonalChatVu mView){
        this.mView = mView;
    }

    @Override
    public void onBackButtonClickListener() {
        mView.closePage();
    }

    @Override
    public void onShowErrorToast(String message) {
        mView.showToast(message);
    }

    @Override
    public void onSendMessageButtonClickListener(String message, long time) {
        mView.createDataToFirestroe(message,time);
    }

    @Override
    public void onCatchChatData(ArrayList<PersonalChatData> chatArrayList) {
        mView.setRecyclerView(chatArrayList);
    }

    @Override
    public void onDataChangeEvent(ArrayList<PersonalChatData> chatArrayList) {
        mView.changeRecyclerView(chatArrayList);
    }
}
