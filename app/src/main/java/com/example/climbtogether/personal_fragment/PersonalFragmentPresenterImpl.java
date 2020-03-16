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
    public void onShowProgress(boolean isShow) {
        mView.showProgress(isShow);
        mView.showLoginInformation(false);
    }



    @Override
    public void onItemClickListener(String displayName, String friendEmail, String photoUrl) {
        mView.intentToPersonalChatActivity(displayName,friendEmail,photoUrl);
    }

    @Override
    public void onShowDeleteMessageConfirmDialog(String documentPath) {
        mView.showDeleteConfirmDialog(documentPath);
    }

    @Override
    public void onCatchallData(ArrayList<String> jsonArray) {

        if (jsonArray.size() == 0){
            Log.i("Michael","沒資料");
            mView.showNoChatDataView(true);
            return;
        }
        Log.i("Michael","開始處理資料");
        ArrayList<PersonalChatObject> dataArrayList = new ArrayList<>();
        for (String json : jsonArray){
            PersonalChatObject data = gson.fromJson(json,PersonalChatObject.class);
            dataArrayList.add(data);
        }

        Log.i("Michael","對話長度 : "+dataArrayList.size());
        ArrayList<PersonalChatDTO> chatArray = new ArrayList<>();
        for (int i = 0 ; i < dataArrayList.size() ; i ++){
            int chatIndex = dataArrayList.get(i).getChatData().size() -1 ;
            PersonalChatDTO data = new PersonalChatDTO();
            if (mView.getUserEmail().equals(dataArrayList.get(i).getUserOneDataDTO().getEmai())){
                if (!mView.getUserEmail().equals(dataArrayList.get(i).getUserTwoDataDTO().getEmai())){
                    data.setDisplayName(dataArrayList.get(i).getUserTwoDataDTO().getDisplayNmae());
                    data.setPhotoUrl(dataArrayList.get(i).getUserTwoDataDTO().getPhotoUrl());
                    data.setFriendEmail(dataArrayList.get(i).getUserTwoDataDTO().getEmai());
                    data.setMessage(dataArrayList.get(i).getChatData().get(chatIndex).getMessage());
                    data.setTime(dataArrayList.get(i).getChatData().get(chatIndex).getTime());
                    chatArray.add(data);
                }
            }else if (mView.getUserEmail().equals(dataArrayList.get(i).getUserTwoDataDTO().getEmai())){
                if (!mView.getUserEmail().equals(dataArrayList.get(i).getUserOneDataDTO().getEmai())){
                    data.setDisplayName(dataArrayList.get(i).getUserOneDataDTO().getDisplayNmae());
                    data.setPhotoUrl(dataArrayList.get(i).getUserOneDataDTO().getPhotoUrl());
                    data.setFriendEmail(dataArrayList.get(i).getUserOneDataDTO().getEmai());
                    data.setMessage(dataArrayList.get(i).getChatData().get(chatIndex).getMessage());
                    data.setTime(dataArrayList.get(i).getChatData().get(chatIndex).getTime());
                    chatArray.add(data);
                }
            }
        }
        mView.showProgress(false);
        if(chatArray.size() != 0){
            mView.setRecyclerView(chatArray);
        }else {
            mView.showNoChatDataView(true);
        }



    }


}
