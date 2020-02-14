package com.example.climbtogether.chat_activity;

import java.util.ArrayList;

public interface ChatActivityVu {
    void searchChatDataFromFirestore(String email);

    void showProgressbar(boolean isShow);

    void setRecyclerView(ArrayList<ChatData> chatDataArrayList);

    void createChatDataToFirestore(String message, long currentTime);

    void showErrorMessage(String errorMessage);
}
