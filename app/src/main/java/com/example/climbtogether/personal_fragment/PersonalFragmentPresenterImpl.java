package com.example.climbtogether.personal_fragment;

import android.util.Log;

import com.example.climbtogether.db_modle.DataBaseApi;
import com.example.climbtogether.db_modle.DataBaseImpl;
import com.example.climbtogether.personal_chat_activity.chat_room_object.PersonalChatData;
import com.example.climbtogether.personal_chat_activity.chat_room_object.PersonalChatObject;
import com.google.gson.Gson;

import java.util.ArrayList;

public class PersonalFragmentPresenterImpl implements PersonalFragmentPresenter {

    private PersonalFragmentVu mView;

    private DataBaseApi dataBaseApi;

    private Gson gson;

    public PersonalFragmentPresenterImpl(PersonalFragmentVu mView) {
        this.mView = mView;
        dataBaseApi = new DataBaseImpl(mView.getVuContext());
        gson = new Gson();
    }

    @Override
    public void onNoUserEvent() {
        mView.showLoginInformation(true);
    }

    @Override
    public void onLoginClickListener() {
        mView.intentToLoginActivity();
    }

    @Override
    public void onSearchUserChatData(String currentEmail, ArrayList<String> friendsArrayList) {
        mView.searchForChatData(currentEmail,friendsArrayList);
    }

    @Override
    public void onShowProgress(boolean isShow) {
        mView.showProgress(isShow);
        mView.showLoginInformation(false);
    }

    @Override
    public void onCatchAllDataSucessful(ArrayList<PersonalChatDTO> chatDataArrayList) {
        mView.showProgress(false);
        Log.i("Michael","資料長度 : "+chatDataArrayList.size());
        if (chatDataArrayList.size() == 0){
            mView.showNoChatDataView(true);
        }else {
            mView.showNoChatDataView(false);
            mView.setRecyclerView(chatDataArrayList);
            insertDatabase(chatDataArrayList);
        }

    }

    private void insertDatabase(ArrayList<PersonalChatDTO> chatDataArrayList) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (dataBaseApi.getAllChatData() == null || dataBaseApi.getAllChatData().size() == 0){
                    for (PersonalChatDTO data : chatDataArrayList){
                        PersonalChatDTO chat = new PersonalChatDTO();
                        chat.setDocumentPath(data.getDocumentPath());
                        chat.setPhotoUrl(data.getPhotoUrl());
                        chat.setDisplayName(data.getDisplayName());
                        chat.setFriendEmail(data.getFriendEmail());
                        chat.setMessage(data.getMessage());
                        chat.setTime(data.getTime());
                        dataBaseApi.insertChatData(chat);
                    }
                    return;
                }

                for (PersonalChatDTO data : dataBaseApi.getAllChatData()){
                    dataBaseApi.deleteChatData(data.getSid());
                }
                for (PersonalChatDTO data : chatDataArrayList){
                    PersonalChatDTO chat = new PersonalChatDTO();
                    chat.setDocumentPath(data.getDocumentPath());
                    chat.setPhotoUrl(data.getPhotoUrl());
                    chat.setDisplayName(data.getDisplayName());
                    chat.setFriendEmail(data.getFriendEmail());
                    chat.setMessage(data.getMessage());
                    chat.setTime(data.getTime());
                    dataBaseApi.insertChatData(chat);
                }
                mView.updateView(dataBaseApi.getAllChatData());
            }
        }).start();







    }

    @Override
    public void onItemClickListener(String displayName, String friendEmail, String photoUrl) {
        mView.intentToPersonalChatActivity(displayName,friendEmail,photoUrl);
    }

    @Override
    public void onNoMessageEvent() {
        mView.showNoChatDataView(true);
    }

    @Override
    public void onShowDeleteMessageConfirmDialog(String documentPath) {
        mView.showDeleteConfirmDialog(documentPath);
    }

    @Override
    public void onCatchChatDataSuccessful(ArrayList<PersonalChatDTO> allChatData) {
        mView.setRecyclerView(allChatData);
        mView.continueSearchData();
    }

    @Override
    public void onCatchallData(ArrayList<String> jsonArray,ArrayList<FriendData> friendDataArray) {

        if (jsonArray.size() == 0){
            Log.i("Michael","沒資料");
            mView.showNoChatDataView(false);
            return;
        }
        Log.i("Michael","開始處理資料");
        ArrayList<PersonalChatObject> dataArrayList = new ArrayList<>();
        for (String json : jsonArray){
            PersonalChatObject data = gson.fromJson(json,PersonalChatObject.class);
            dataArrayList.add(data);
        }

        ArrayList<PersonalChatDTO> chatArray = new ArrayList<>();
        for (int i = 0 ; i < dataArrayList.size() ; i ++){
            int chatIndex = dataArrayList.get(i).getChatData().size() -1 ;
            PersonalChatDTO data = new PersonalChatDTO();
            data.setDisplayName(friendDataArray.get(i).getdisplayName());
            Log.i("Michael","friendName :" +friendDataArray.get(i).getdisplayName());
            data.setPhotoUrl(friendDataArray.get(i).getPhotoUrl());
            data.setFriendEmail(friendDataArray.get(i).getEmail());
            data.setMessage(dataArrayList.get(i).getChatData().get(chatIndex).getMessage());
            data.setTime(dataArrayList.get(i).getChatData().get(chatIndex).getTime());
            chatArray.add(data);
        }
        mView.showProgress(false);
        mView.setRecyclerView(chatArray);

    }


}
