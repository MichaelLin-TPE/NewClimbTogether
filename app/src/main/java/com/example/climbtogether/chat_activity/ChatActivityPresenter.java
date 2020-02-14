package com.example.climbtogether.chat_activity;

import java.util.ArrayList;

public interface ChatActivityPresenter {
    void onBtnSendClickListener(String message, long currentTime);

    void onSearchChatData(String email);

    void onCatchChatDataSuccessful(ArrayList<ChatData> chatDataArrayList);
}
