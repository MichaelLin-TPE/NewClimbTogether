package com.hiking.climbtogether.chat_activity;

import java.util.ArrayList;

public interface ChatActivityVu {

    void showProgressbar(boolean isShow);

    void setRecyclerView(ArrayList<ChatData> chatDataArrayList);

    void showErrorMessage(String errorMessage);

    void catchDataFormFirestore(String email);

    void reShowRecyclerView(ArrayList<ChatData> chatDataArrayList);

    void searchInfoFromFirebase(String mail);

    void showUserDialog(String displayName, String photoUrl, String mail,boolean isFriend);

    void setUserDialogViewChange(boolean b);

    void searchFriendShip(String email,String strangerEmail);

    void sendInviteToStranger(String StrangerEmail,String userEmail);

    void showToast(String message);

    void searchFriendInvite(String strangeEmail);

    void intentToPersonalChatActivity(String displayName, String mail, String photoUrl);

    void showUserDialog();

    void createNewChatDataToFirebase(String jsonStr);

    void intentToVoteActivity();

    void intentToVoteListActivity();
}
