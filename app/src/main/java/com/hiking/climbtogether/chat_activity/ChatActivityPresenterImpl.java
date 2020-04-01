package com.hiking.climbtogether.chat_activity;

import com.google.gson.Gson;
import com.hiking.climbtogether.ChatObject;
import com.hiking.climbtogether.tool.UserDataManager;

import java.util.ArrayList;

public class ChatActivityPresenterImpl implements ChatActivityPresenter {

    private ChatActivityVu mView;

    private String friendEmail;

    private Gson gson;

    private ArrayList<ChatData> dataArrayList;

    public ChatActivityPresenterImpl(ChatActivityVu mView){
        this.mView = mView;
        gson = new Gson();
    }

    @Override
    public void onBtnSendClickListener(String message, long currentTime, UserDataManager userDataManager) {
        if (message == null || message.isEmpty()){
            String errorMessage = "請輸入對話";
            mView.showErrorMessage(errorMessage);
            return;
        }
        if (dataArrayList == null || dataArrayList.size() == 0){
            ChatObject object = new ChatObject();
            ChatData data = new ChatData();
            data.setDisPlayName(userDataManager.getDisplayName());
            data.setEmail(userDataManager.getEmail());
            data.setMessage(message);
            data.setPhotoUrl(userDataManager.getPhotoUrl());
            data.setTime(currentTime);
            ArrayList<ChatData> dataArrayList = new ArrayList<>();
            dataArrayList.add(data);
            object.setChatData(dataArrayList);
            String jsonStr = gson.toJson(object);

            mView.createNewChatDataToFirebase(jsonStr);
        }else {
            ChatObject object = new ChatObject();
            ChatData data = new ChatData();
            data.setDisPlayName(userDataManager.getDisplayName());
            data.setEmail(userDataManager.getEmail());
            data.setMessage(message);
            data.setPhotoUrl(userDataManager.getPhotoUrl());
            data.setTime(currentTime);
            dataArrayList.add(data);
            object.setChatData(dataArrayList);
            String jsonStr = gson.toJson(object);
            mView.createNewChatDataToFirebase(jsonStr);

        }

    }



    @Override
    public void onChangeData(String email) {
        mView.catchDataFormFirestore(email);
    }

    @Override
    public void onUserPhotoClickListener(String mail) {
        this.friendEmail = mail;
        mView.showUserDialog();

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

    @Override
    public void onSearUserData() {
        mView.searchInfoFromFirebase(friendEmail);
    }

    @Override
    public void onCatchNewChatData(String jsonStr) {
        ChatObject data = gson.fromJson(jsonStr,ChatObject.class);
        this.dataArrayList = data.getChatData();
        mView.setRecyclerView(data.getChatData());
    }

    @Override
    public void onCatchNoChatData() {

    }

    @Override
    public void onCreateVoteButtonClickListener() {
        mView.intentToVoteActivity();
    }

    @Override
    public void onVoteListClickListener() {
        mView.intentToVoteListActivity();
    }
}
