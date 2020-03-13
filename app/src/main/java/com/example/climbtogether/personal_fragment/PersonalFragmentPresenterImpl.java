package com.example.climbtogether.personal_fragment;

import android.util.Log;

import com.example.climbtogether.db_modle.DataBaseApi;
import com.example.climbtogether.db_modle.DataBaseImpl;

import java.util.ArrayList;

public class PersonalFragmentPresenterImpl implements PersonalFragmentPresenter {

    private PersonalFragmentVu mView;

    private DataBaseApi dataBaseApi;

    public PersonalFragmentPresenterImpl(PersonalFragmentVu mView) {
        this.mView = mView;
        dataBaseApi = new DataBaseImpl(mView.getVuContext());
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


}
