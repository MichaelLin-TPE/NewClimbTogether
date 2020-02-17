package com.example.climbtogether.personal_chat_activity;

import java.util.ArrayList;

public interface PersonalChatPresenter {
    void onBackButtonClickListener();

    void onShowErrorToast(String message);

    void onSendMessageButtonClickListener(String message, long time);

    void onCatchChatData(ArrayList<PersonalChatData> chatArrayList);

    void onDataChangeEvent( ArrayList<PersonalChatData> chatArrayList);
}
