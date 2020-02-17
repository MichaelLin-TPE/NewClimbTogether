package com.example.climbtogether.chat_activity;

import java.util.ArrayList;

public interface ChatActivityPresenter {
    void onBtnSendClickListener(String message, long currentTime);

    void onSearchChatData(String email);

    void onCatchChatDataSuccessful(ArrayList<ChatData> chatDataArrayList);

    void onChangeData(String email);

    void onShowRecyclerViewChangeData(ArrayList<ChatData> chatDataArrayList);

    void onUserPhotoClickListener(String mail);

    void onCatchUserData(String displayName, String photoUrl, String mail,boolean isFriend);

    void onAddFriendClickListener(String StrangerEmail,String userEmail);

    void onSearchFriendShip(String email,String strangerEmail);

    void onSendInviteSuccessful();

    void onSearchInvite(String strangeEmail);

    void onChatButtonClickListener(String displayName, String mail, String photoUrl);
}
