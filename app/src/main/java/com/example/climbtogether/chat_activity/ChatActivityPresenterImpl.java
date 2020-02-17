package com.example.climbtogether.chat_activity;

import java.util.ArrayList;

public class ChatActivityPresenterImpl implements ChatActivityPresenter {

    private ChatActivityVu mView;

    public ChatActivityPresenterImpl(ChatActivityVu mView){
        this.mView = mView;
    }

    @Override
    public void onBtnSendClickListener(String message, long currentTime) {
        if (message == null || message.isEmpty()){
            String errorMessage = "請輸入對話";
            mView.showErrorMessage(errorMessage);
            return;
        }
        mView.createChatDataToFirestore(message,currentTime);
    }

    @Override
    public void onSearchChatData(String email) {
        mView.searchChatDataFromFirestore(email);
    }

    @Override
    public void onCatchChatDataSuccessful(ArrayList<ChatData> chatDataArrayList) {
        mView.setRecyclerView(chatDataArrayList);
    }

    @Override
    public void onChangeData(String email) {
        mView.catchDataFormFirestore(email);
    }

    @Override
    public void onShowRecyclerViewChangeData(ArrayList<ChatData> chatDataArrayList) {
        mView.reShowRecyclerView(chatDataArrayList);
    }

    @Override
    public void onUserPhotoClickListener(String mail) {
        mView.searchInfoFromFirebase(mail);
    }

    @Override
    public void onCatchUserData(String displayName, String photoUrl, String mail,boolean isFriend) {
        mView.showUserDialog(displayName,photoUrl,mail,isFriend);
    }

    @Override
    public void onAddFriendClickListener(String strangerEmail,String userEmail) {
        mView.sendInviteToStranger(strangerEmail,userEmail);
    }


    @Override
    public void onSearchFriendShip(String email, String strangerEmail) {
        mView.searchFriendShip(email,strangerEmail);
    }

    @Override
    public void onSendInviteSuccessful() {
        mView.setUserDialogViewChange(true);
        String message = "邀請成功";
        mView.showToast(message);
    }

    @Override
    public void onSearchInvite(String strangeEmail) {
        mView.searchFriendInvite(strangeEmail);
    }

    @Override
    public void onChatButtonClickListener(String displayName, String mail, String photoUrl) {
        mView.intentToPersonalChatActivity(displayName,mail,photoUrl);
    }
}
