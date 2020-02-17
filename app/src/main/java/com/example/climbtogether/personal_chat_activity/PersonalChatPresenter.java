package com.example.climbtogether.personal_chat_activity;

public interface PersonalChatPresenter {
    void onBackButtonClickListener();

    void onShowErrorToast(String message);

    void onSendMessageButtonClickListener(String message, long time);
}
