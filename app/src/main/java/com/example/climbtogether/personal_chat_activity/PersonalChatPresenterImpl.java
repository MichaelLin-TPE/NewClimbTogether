package com.example.climbtogether.personal_chat_activity;

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
}
