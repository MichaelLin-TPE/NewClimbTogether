package com.example.climbtogether.share_activity;

import android.util.Log;

import com.example.climbtogether.share_activity.share_json.ShareArticleJson;
import com.example.climbtogether.share_activity.share_json.ShareClickLikeObject;
import com.example.climbtogether.tool.UserDataManager;
import com.google.gson.Gson;

import java.util.ArrayList;

public class ShareActivityPresenterImpl implements ShareActivityPresenter {

    private ShareActivityVu mView;

    private ShareArticleJson data;

    private Gson gson;

    public ShareActivityPresenterImpl(ShareActivityVu mView) {
        this.mView = mView;
        gson = new Gson();
    }

    @Override
    public void onBackButtonClick() {
        mView.closePage();
    }

    @Override
    public void onAddArticleClick() {
        mView.onShowAddArticleDialog();
    }

    @Override
    public void onShareButtonClick(UserDataManager userDataManager, String content, ArrayList<byte[]> photoBytesArray) {
        if (content == null || content.isEmpty()){
            String message = "請輸入內容";
            mView.showErrorMessage(message);
            return;
        }
        String message = "上傳中....請稍後";
        mView.showProgressMessage(message);
        mView.uploadPhoto(userDataManager,content,photoBytesArray);


    }

    @Override
    public void onShowSuccessShareArticle() {
        String message = "分享成功";
        mView.showErrorMessage(message);
    }

    @Override
    public void onShowProgress() {
        mView.showProgress(true);
    }

    @Override
    public void onCloseProgress() {
        mView.showProgress(false);
    }

    @Override
    public void onReplyButtonClick(ShareArticleJson shareArticleDTO) {
        mView.showReplayDialog(shareArticleDTO);
    }

    @Override
    public void onButtonSendReplyClick(ArrayList<ReplyDTO> replyArray, String content, ShareArticleJson shareArticleDTO) {
        if (content == null || content.isEmpty()){
            String message = "留下些甚麼吧...";
            mView.showErrorMessage(message);
            return;
        }
        mView.sendReply(content,replyArray,shareArticleDTO);


    }

    @Override
    public void onUserPhotoClickListener(ShareArticleJson data) {
        this.data = data;
        mView.showUserDialog();

    }

    @Override
    public void onShowUserDialog(ShareArticleJson data, boolean isInvite, boolean isFriend) {
        mView.setProgressStart(false);
        mView.showUserDialog(data,isInvite,isFriend);
    }

    @Override
    public void onSetDialogViewChange() {
        mView.setProgressStart(true);
    }

    @Override
    public void onSearchFriendShip() {
        mView.searchForFriendship(data);
    }

    @Override
    public void onAddFriendButtonClickListener(String strangerEmail, String userEmail) {
        mView.sendInviteToStranger(strangerEmail,userEmail);
    }

    @Override
    public void onSendMessageClickListener(ShareArticleJson data) {
        mView.checkFriendship(data);
    }

    @Override
    public void onIsFriend(ShareArticleJson data, boolean isFriendSend) {
        this.data = data;
        if (isFriendSend){
            Log.i("Michael","是朋友");
            mView.intentToPersonalChatActivity(data);
        }else {
            Log.i("Michael","不是朋友");
            mView.showNoticeDialog(data);
        }
    }

    @Override
    public void onCatchAllJson(ArrayList<String> jsonStrArray) {

        ArrayList<ShareArticleJson> dataArrayList = new ArrayList<>();

        for (String jsonStr : jsonStrArray){
            ShareArticleJson data = gson.fromJson(jsonStr,ShareArticleJson.class);
            Log.i("Michael","content : "+data.getContent());
            dataArrayList.add(data);
        }
        mView.setNewRecyclerView(dataArrayList);
    }

    @Override
    public void onCatchallPhotoUrl(UserDataManager userDataManager, String content, ArrayList<String> photoUrlArray) {
        ShareArticleJson json = new ShareArticleJson();
        json.setContent(content);
        json.setEmail(userDataManager.getEmail());
        json.setLike(0);
        json.setReply(0);
        json.setUserPhoto(userDataManager.getPhotoUrl());
        json.setSharePhoto(photoUrlArray);
        json.setClick_member(new ArrayList<ShareClickLikeObject>());
        json.setDisplayName(userDataManager.getDisplayName());
        String jsonStr = gson.toJson(json);

        mView.shareArticleJson(jsonStr,json.getContent());


        Log.i("Michael","json 格式 : "+jsonStr);
    }
}
