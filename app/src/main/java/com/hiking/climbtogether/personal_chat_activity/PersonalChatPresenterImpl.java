package com.hiking.climbtogether.personal_chat_activity;

import android.util.Log;

import com.hiking.climbtogether.my_equipment_activity.FriendData;
import com.hiking.climbtogether.personal_chat_activity.chat_room_object.ChatRoomDTO;
import com.hiking.climbtogether.personal_chat_activity.chat_room_object.PersonalChatData;
import com.hiking.climbtogether.personal_chat_activity.chat_room_object.PersonalChatObject;
import com.hiking.climbtogether.personal_chat_activity.chat_room_object.UserOneDataDTO;
import com.hiking.climbtogether.personal_chat_activity.chat_room_object.UserTwoDataDTO;
import com.hiking.climbtogether.personal_chat_activity.fcm_object.FcmData;
import com.hiking.climbtogether.personal_chat_activity.fcm_object.FcmNotification;
import com.hiking.climbtogether.personal_chat_activity.fcm_object.FcmObject;
import com.hiking.climbtogether.tool.HttpConnection;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Locale;

public class PersonalChatPresenterImpl implements PersonalChatPresenter {

    private PersonalChatVu mView;

    private Gson gson;

    private ArrayList<PersonalChatObject> dataArrayList;

    private ArrayList<PersonalChatData> chatDataArrayList;

    private boolean isOpenToolsView;


    public PersonalChatPresenterImpl(PersonalChatVu mView){
        this.mView = mView;
        gson = new Gson();
    }

    @Override
    public void onBackButtonClickListener() {
        mView.closePage();
    }

    @Override
    public void onShowErrorToast(String message) {
        mView.showToast(message);
    }

    @Override
    public void onSendMessageButtonClickListener(String message, long time) {
        //新方法
        mView.setDataToFireStore(message,time);
    }

    @Override
    public void onCatchChatData(ArrayList<PersonalChatData> chatArrayList) {
        mView.setRecyclerView(chatArrayList);
    }

    @Override
    public void onDataChangeEvent(ArrayList<PersonalChatData> chatArrayList) {
        mView.changeRecyclerView(chatArrayList);
    }

    @Override
    public void onSendNotificationToFriend(String friendEmail,String message,String displayName) {
        mView.searchFriendData(friendEmail,message,displayName);
    }

    @Override
    public void onPostFcmToFriend(String token, String message, String displayName) {
        HttpConnection connection = new HttpConnection();
        String url = "https://fcm.googleapis.com/fcm/send";
        FcmObject fcmObject = new FcmObject();
        FcmData fcmData = new FcmData();
        fcmData.setDataContent(message);
        fcmData.setDataTitle(displayName);
        fcmObject.setTo(token);
//        fcmObject.setCollapsekey("type_a");
        FcmNotification data = new FcmNotification();
        data.setBody(message);
        data.setTitle(mView.getDisplayName());
        fcmObject.setNotification(data);
        fcmObject.setData(fcmData);
        String jsonStr = new Gson().toJson(fcmObject);

        Log.i("Michael",jsonStr);
        connection.startConnection(url, jsonStr, new HttpConnection.OnPostNotificationListener() {
            @Override
            public void onSuccessful(String result) {
                Log.i("Michael",result);
            }

            @Override
            public void onFail(String exception) {
                Log.i("Michael","錯誤 : "+exception);
            }
        });
    }

    @Override
    public void onCatchChatJson(String jsonStr) {
        Log.i("Michael","抓到 json : "+jsonStr);
        dataArrayList = new ArrayList<>();
        PersonalChatObject data = gson.fromJson(jsonStr,PersonalChatObject.class);
        dataArrayList.add(data);
        mView.setRecyclerView(dataArrayList.get(0).getChatData());
        this.chatDataArrayList = data.getChatData();
    }

    @Override
    public void sendMessage(String message, long time, String path, String userPhotoUrl, String userDisplayName, String friendEmail, String friendDisplayName, String friendPhotoUrl) {
        if (dataArrayList == null){
            PersonalChatData data = new PersonalChatData();
            ArrayList<PersonalChatData> chatArrayList = new ArrayList<>();
            PersonalChatObject object = new PersonalChatObject();
            UserOneDataDTO user1 = new UserOneDataDTO();
            UserTwoDataDTO user2 = new UserTwoDataDTO();
            user1.setDisplayNmae(userDisplayName);
            user1.setEmai(mView.getEmail());
            user1.setPhotoUrl(userPhotoUrl);
            user2.setDisplayNmae(friendDisplayName);
            user2.setEmai(friendEmail);
            user2.setPhotoUrl(friendPhotoUrl);
            data.setEmail(mView.getEmail());
            data.setMessage(message);
            data.setTime(time);
            data.setPhotoUrl(friendPhotoUrl);
            data.setDisplayName(userDisplayName);
            data.setImageUrl(new ArrayList<>());
            chatArrayList.add(data);
            object.setUserOneDataDTO(user1);
            object.setUserTwoDataDTO(user2);
            object.setChatData(chatArrayList);
            String jsonStr = gson.toJson(object);
            mView.setChatDataToFireStore(jsonStr);
        }else {
            PersonalChatData data = new PersonalChatData();
            data.setEmail(mView.getEmail());
            data.setMessage(message);
            data.setTime(time);
            data.setImageUrl(new ArrayList<>());
            data.setPhotoUrl(friendPhotoUrl);
            data.setDisplayName(userDisplayName);
            dataArrayList.get(0).getChatData().add(data);
            String jsonStr = gson.toJson(dataArrayList.get(0));
            mView.setChatDataToFireStore(jsonStr);
        }
    }

    @Override
    public void onSendPhotoButtonClickListener() {
        mView.showPhotoPage();
    }

    @Override
    public void onCatchAllPhoto(ArrayList<byte[]> photoBytesArray) {
        mView.uploadPhoto(photoBytesArray);
    }

    @Override
    public void onCatchUploadError(String toString) {
        String message = String.format(Locale.getDefault(),"上傳失敗,請確認網路狀況再重試一次,錯誤代碼 : %s",toString);
        mView.showErrorCode(message);
    }

    @Override
    public void onShowProgressMessage(String message) {
        mView.showErrorCode(message);
    }

    @Override
    public void onCatchAllPhotoUrl(String message, long time, String path, String userPhotoUrl, String userDisplayName, String friendEmail, String friendDisplayName, String friendPhotoUrl,ArrayList<String> downloadUrlArray) {
        if (dataArrayList == null){
            PersonalChatData data = new PersonalChatData();
            ArrayList<PersonalChatData> chatArrayList = new ArrayList<>();
            PersonalChatObject object = new PersonalChatObject();
            UserOneDataDTO user1 = new UserOneDataDTO();
            UserTwoDataDTO user2 = new UserTwoDataDTO();
            user1.setDisplayNmae(userDisplayName);
            user1.setEmai(mView.getEmail());
            user1.setPhotoUrl(userPhotoUrl);
            user2.setDisplayNmae(friendDisplayName);
            user2.setEmai(friendEmail);
            user2.setPhotoUrl(friendPhotoUrl);
            data.setEmail(mView.getEmail());
            data.setMessage(message);
            data.setTime(time);
            data.setPhotoUrl(friendPhotoUrl);
            data.setDisplayName(userDisplayName);
            data.setImageUrl(downloadUrlArray);
            chatArrayList.add(data);
            object.setUserOneDataDTO(user1);
            object.setUserTwoDataDTO(user2);
            object.setChatData(chatArrayList);
            String jsonStr = gson.toJson(object);
            mView.setChatDataToFireStore(jsonStr);
        }else {
            PersonalChatData data = new PersonalChatData();
            data.setEmail(mView.getEmail());
            data.setMessage(message);
            data.setTime(time);
            data.setImageUrl(downloadUrlArray);
            data.setPhotoUrl(friendPhotoUrl);
            data.setDisplayName(userDisplayName);
            dataArrayList.get(0).getChatData().add(data);
            String jsonStr = gson.toJson(dataArrayList.get(0));
            mView.setChatDataToFireStore(jsonStr);
        }
    }

    @Override
    public void onPhotoClickListener(String downLoadUrl) {
        mView.intentToPhotoActivity(downLoadUrl);
    }

    @Override
    public void onCameraButtonClickListener() {
        mView.showCamera();
    }

    @Override
    public void onShareButtonClickListener(ArrayList<String> downloadUrl) {
        mView.showBottomShareView(downloadUrl);
    }

    @Override
    public void onTouchScreenEvent(boolean isShowBottomView) {
        isOpenToolsView = false;
        mView.closeAllToolsView();
        if (isShowBottomView){
            mView.closeBottomView(true);
        }
    }

    @Override
    public void onShareUserClickListener(FriendData data, ArrayList<ChatRoomDTO> chatRoomArray, ArrayList<String> downloadUrl) {
        String path = "";

        for (ChatRoomDTO chatData : chatRoomArray){
            if (chatData.getUser1().equals(data.getEmail()) && chatData.getUser2().equals(mView.getEmail())){
                path = chatData.getDocument();
                break;
            }else if (chatData.getUser1().equals(mView.getEmail())&&chatData.getUser2().equals(data.getEmail())){
                path = chatData.getDocument();
                break;
            }
        }

        mView.addPhoto(data.getEmail(),data.getName(),data.getPhoto(),downloadUrl,path);

        mView.intentToPersonalChatActivity(data.getEmail(),data.getName(),data.getPhoto(),path);
    }

    @Override
    public void onCatchFriendJson(String json, String email, String name, String photo, ArrayList<String> downloadUrl, String path) {
        if (json != null){
            PersonalChatObject data = gson.fromJson(json,PersonalChatObject.class);
            ArrayList<PersonalChatData> chatDataArray = data.getChatData();
            PersonalChatData chat = new PersonalChatData();
            chat.setImageUrl(downloadUrl);
            chat.setDisplayName(mView.getDisplayName());
            chat.setEmail(mView.getEmail());
            chat.setMessage("");
            chat.setPhotoUrl(mView.getPhotoUrl());
            chat.setTime(System.currentTimeMillis());
            chatDataArray.add(chat);
            data.setChatData(chatDataArray);
            String jsonStr = gson.toJson(data);
            mView.updateFriendChatData(jsonStr,path);
        }else {
            PersonalChatData data = new PersonalChatData();
            ArrayList<PersonalChatData> chatArrayList = new ArrayList<>();
            PersonalChatObject object = new PersonalChatObject();
            UserOneDataDTO user1 = new UserOneDataDTO();
            UserTwoDataDTO user2 = new UserTwoDataDTO();
            user1.setDisplayNmae(mView.getDisplayName());
            user1.setEmai(mView.getEmail());
            user1.setPhotoUrl(mView.getPhotoUrl());
            user2.setDisplayNmae(name);
            user2.setEmai(email);
            user2.setPhotoUrl(photo);
            data.setEmail(mView.getEmail());
            data.setMessage("");
            data.setTime(System.currentTimeMillis());
            data.setPhotoUrl(mView.getPhotoUrl());
            data.setDisplayName(mView.getDisplayName());
            data.setImageUrl(downloadUrl);
            chatArrayList.add(data);
            object.setUserOneDataDTO(user1);
            object.setUserTwoDataDTO(user2);
            object.setChatData(chatArrayList);
            String jsonStr = gson.toJson(object);
            mView.updateFriendChatData(jsonStr,path);
        }
    }

    @Override
    public void onToolsButtonClickListener() {
        if (!isOpenToolsView){
            mView.showToolsListView();
            isOpenToolsView = true;
        }else {
            isOpenToolsView = false;
            mView.closeToolsList(true);
        }

    }

    @Override
    public void onToolsListClickListener(String name) {
        if (name.equals(mView.getSearchStr())){
            isOpenToolsView = false;
            mView.closeToolsList(true);
            mView.showSearchDataView(true);
        }
        if (name.equals(mView.getPictureStr())){
            isOpenToolsView = false;
            mView.closeToolsList(true);
            ArrayList<String> photoUrlArray = new ArrayList<>();
            for (PersonalChatData data : chatDataArrayList){
                if (data.getImageUrl() != null){
                    photoUrlArray.addAll(data.getImageUrl());
                }
            }
            if (photoUrlArray.size() != 0){
                mView.intentToPersonalChatImageActivity(photoUrlArray);
            }else {
                String message = "沒有任何照片唷";
                mView.showErrorCode(message);
            }

        }
    }

    @Override
    public void onSearchContentListener(String content) {
        mView.hideKeyBoard();
        ArrayList<Integer> searchContentIndexArray = new ArrayList<>();
        int index = 0;
        for (PersonalChatData data : chatDataArrayList){
            if (data.getMessage().contains(content)){
                searchContentIndexArray.add(index);
            }
            index ++;
        }
        if (searchContentIndexArray.size() != 0){
            mView.showSearchResult(searchContentIndexArray);
        }else {
            mView.showSearchNoChatDataDialog();
        }
    }

    @Override
    public void onUpClickListener(ArrayList<Integer> searchContentIndexArray, int contentIndex) {
        contentIndex ++;
        if (contentIndex < searchContentIndexArray.size()){
            mView.scrollToPosition(searchContentIndexArray.get(contentIndex));
        }else {
            contentIndex --;
            mView.scrollToPosition(searchContentIndexArray.get(contentIndex));
        }
    }

    @Override
    public void onDownClickListener(ArrayList<Integer> searchContentIndexArray, int contentIndex) {
        contentIndex --;
        if (contentIndex < 0){
            contentIndex = 0;
        }
        if (contentIndex < searchContentIndexArray.size()){
            mView.scrollToPosition(searchContentIndexArray.get(contentIndex));
        }else {
            contentIndex ++;
            mView.scrollToPosition(searchContentIndexArray.get(contentIndex));
        }
    }
}
