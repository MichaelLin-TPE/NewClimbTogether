package com.example.climbtogether.personal_chat_activity;

import android.util.Log;

import com.example.climbtogether.personal_chat_activity.fcm_object.FcmData;
import com.example.climbtogether.personal_chat_activity.fcm_object.FcmNotification;
import com.example.climbtogether.personal_chat_activity.fcm_object.FcmObject;
import com.example.climbtogether.tool.HttpConnection;
import com.google.gson.Gson;

import java.net.HttpURLConnection;
import java.util.ArrayList;

public class PersonalChatPresenterImpl implements PersonalChatPresenter {

    private PersonalChatVu mView;

    public PersonalChatPresenterImpl(PersonalChatVu mView){
        this.mView = mView;
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
        mView.createDataToFirestroe(message,time);
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
}
