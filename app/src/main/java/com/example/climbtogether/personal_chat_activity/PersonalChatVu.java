package com.example.climbtogether.personal_chat_activity;

import java.util.ArrayList;

public interface PersonalChatVu {
    void closePage();

    void showToast(String message);

    void createDataToFirestroe(String message, long time);

    void setRecyclerView(ArrayList<PersonalChatData> chatArrayList);

    void changeRecyclerView(ArrayList<PersonalChatData> chatArrayList);

    void searchFriendData(String friendEmail,String message,String displayName);

    String getDisplayName();
}
