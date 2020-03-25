package com.hiking.climbtogether.personal_chat_activity;

import android.util.Log;

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
}
