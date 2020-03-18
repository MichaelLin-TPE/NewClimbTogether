package com.hiking.climbtogether.chat_activity;

import com.hiking.climbtogether.tool.UserDataManager;

import java.util.ArrayList;

public interface ChatActivityPresenter {
    void onBtnSendClickListener(String message, long currentTime, UserDataManager userDataManager);

    void onChangeData(String email);

    void onUserPhotoClickListener(String mail);

    void onCatchUserData(String displayName, String photoUrl, String mail,boolean isFriend);

    void onAddFriendClickListener(String StrangerEmail,String userEmail);

    void onSearchFriendShip(String email,String strangerEmail);

    void onSendInviteSuccessful();

    void onSearchInvite(String strangeEmail);

    void onChatButtonClickListener(String displayName, String mail, String photoUrl);

    void onSearUserData();

    void onCatchNewChatData(String jsonStr);

    void onCatchNoChatData();
}
