package com.hiking.climbtogether.personal_chat_activity;

import com.hiking.climbtogether.personal_chat_activity.chat_room_object.PersonalChatData;

import java.util.ArrayList;

public interface PersonalChatVu {
    void closePage();

    void showToast(String message);
    void setRecyclerView(ArrayList<PersonalChatData> chatArrayList);

    void changeRecyclerView(ArrayList<PersonalChatData> chatArrayList);

    void searchFriendData(String friendEmail,String message,String displayName);

    String getDisplayName();

    void setDataToFireStore(String message, long time);

    String getEmail();

    void setChatDataToFireStore(String jsonStr);
}
