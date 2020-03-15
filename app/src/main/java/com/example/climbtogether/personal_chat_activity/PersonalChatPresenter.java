package com.example.climbtogether.personal_chat_activity;

import com.example.climbtogether.personal_chat_activity.chat_room_object.PersonalChatData;

import java.util.ArrayList;

public interface PersonalChatPresenter {
    void onBackButtonClickListener();

    void onShowErrorToast(String message);

    void onSendMessageButtonClickListener(String message, long time);

    void onCatchChatData(ArrayList<PersonalChatData> chatArrayList);

    void onDataChangeEvent( ArrayList<PersonalChatData> chatArrayList);

    void onSendNotificationToFriend(String friendEmail,String message,String displayName);

    void onPostFcmToFriend(String token, String message, String displayName);

    void onCatchChatJson(String jsonStr);

    void sendMessage(String message, long time, String testPath, String photoUrl, String displayName);
}
