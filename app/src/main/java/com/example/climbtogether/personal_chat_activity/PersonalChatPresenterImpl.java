package com.example.climbtogether.personal_chat_activity;

import android.util.Log;

import com.example.climbtogether.personal_chat_activity.chat_room_object.PersonalChatData;
import com.example.climbtogether.personal_chat_activity.chat_room_object.PersonalChatObject;
import com.example.climbtogether.personal_chat_activity.fcm_object.FcmData;
import com.example.climbtogether.personal_chat_activity.fcm_object.FcmNotification;
import com.example.climbtogether.personal_chat_activity.fcm_object.FcmObject;
import com.example.climbtogether.tool.HttpConnection;
import com.google.gson.Gson;

import java.util.ArrayList;

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
        dataArrayList = new ArrayList<>();
        PersonalChatObject data = gson.fromJson(jsonStr,PersonalChatObject.class);
        dataArrayList.add(data);
        mView.setRecyclerView(dataArrayList.get(0).getChatData());

    }

    @Override
    public void sendMessage(String message, long time, String documentPath) {
        if (dataArrayList == null){
            PersonalChatData data = new PersonalChatData();
            ArrayList<PersonalChatData> chatArrayList = new ArrayList<>();
            PersonalChatObject object = new PersonalChatObject();
            data.setEmail(mView.getEmail());
            data.setMessage(message);
            data.setTime(time);
            chatArrayList.add(data);
            object.setChatData(chatArrayList);
            String jsonStr = gson.toJson(object);
            mView.setChatDataToFireStore(jsonStr);
        }else {
            PersonalChatData data = new PersonalChatData();
            data.setEmail(mView.getEmail());
            data.setMessage(message);
            data.setTime(time);
            dataArrayList.get(0).getChatData().add(data);
            String jsonStr = gson.toJson(dataArrayList.get(0));
            mView.setChatDataToFireStore(jsonStr);
        }


    }
}
