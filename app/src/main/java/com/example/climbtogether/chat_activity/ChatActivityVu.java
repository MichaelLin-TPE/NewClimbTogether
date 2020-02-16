package com.example.climbtogether.chat_activity;

import java.util.ArrayList;

public interface ChatActivityVu {
    void searchChatDataFromFirestore(String email);

    void showProgressbar(boolean isShow);

    void setRecyclerView(ArrayList<ChatData> chatDataArrayList);

    void createChatDataToFirestore(String message, long currentTime);

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
}
