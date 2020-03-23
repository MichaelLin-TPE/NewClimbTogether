package com.hiking.climbtogether.share_activity;

import android.util.Log;

import com.hiking.climbtogether.share_activity.share_json.ShareArticleJson;
import com.hiking.climbtogether.share_activity.share_json.ShareClickLikeObject;
import com.hiking.climbtogether.tool.UserDataManager;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Locale;

public class ShareActivityPresenterImpl implements ShareActivityPresenter {

    private ShareActivityVu mView;

    private ShareArticleJson data;

    private Gson gson;

    private static final int EDIT = 1;

    private static final int DELETE = 0;

    private static final int IMPEACHMENT = 0;

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
        if (content == null || content.isEmpty()) {
            String message = "請輸入內容";
            mView.showErrorMessage(message);
            return;
        }
        String message = "上傳中....請稍後";
        mView.showProgressMessage(message);
        mView.uploadPhoto(userDataManager, content, photoBytesArray);


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
        if (content == null || content.isEmpty()) {
            String message = "留下些甚麼吧...";
            mView.showErrorMessage(message);
            return;
        }
        mView.sendReply(content, replyArray, shareArticleDTO);


    }

    @Override
    public void onUserPhotoClickListener(ShareArticleJson data) {
        this.data = data;
        mView.showUserDialog();

    }

    @Override
    public void onShowUserDialog(ShareArticleJson data, boolean isInvite, boolean isFriend) {
        mView.setProgressStart(false);
        mView.showUserDialog(data, isInvite, isFriend);
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
        mView.sendInviteToStranger(strangerEmail, userEmail);
    }

    @Override
    public void onSendMessageClickListener(ShareArticleJson data) {
        mView.checkFriendship(data);
    }

    @Override
    public void onIsFriend(ShareArticleJson data, boolean isFriendSend) {
        this.data = data;
        if (isFriendSend) {
            Log.i("Michael", "是朋友");
            mView.intentToPersonalChatActivity(data);
        } else {
            Log.i("Michael", "不是朋友");
            mView.showNoticeDialog(data);
        }
    }

    @Override
    public void onCatchAllJson(ArrayList<String> jsonStrArray) {

        ArrayList<ShareArticleJson> dataArrayList = new ArrayList<>();

        for (String jsonStr : jsonStrArray) {
            ShareArticleJson data = gson.fromJson(jsonStr, ShareArticleJson.class);
            Log.i("Michael", "content : " + data.getContent());
            dataArrayList.add(data);
        }
        mView.showNoDataView(false);
        mView.setNewRecyclerView(dataArrayList);
    }

    @Override
    public void onCatchallPhotoUrl(UserDataManager userDataManager, String content, ArrayList<String> photoUrlArray) {
        ShareArticleJson json = new ShareArticleJson();
        json.setOldContent(content);
        json.setContent(content);
        json.setEmail(userDataManager.getEmail());
        json.setLike(0);
        json.setReply(0);
        json.setUserPhoto(userDataManager.getPhotoUrl());
        json.setSharePhoto(photoUrlArray);
        json.setClick_member(new ArrayList<ShareClickLikeObject>());
        json.setDisplayName(userDataManager.getDisplayName());
        String jsonStr = gson.toJson(json);
        long currentTime = System.currentTimeMillis();
        mView.shareArticleJson(jsonStr, json.getContent(), currentTime);


        Log.i("Michael", "json 格式 : " + jsonStr);
    }

    @Override
    public void onSettingButtonClickListener(ShareArticleJson data, int itemPosition) {
        if (data.getEmail().equals(mView.getUserEmail())) {
            Log.i("Michael", "我的文章");
            ArrayList<String> dialogList = new ArrayList<>();
            dialogList.add(mView.getDeleteStr());
            dialogList.add(mView.getEditStr());

            mView.showUserArticleDialog(dialogList,data,itemPosition);
        } else {
            ArrayList<String> dialogList = new ArrayList<>();
            dialogList.add(mView.getImpeachment());
            mView.showStrangerArticleDialog(dialogList,data,itemPosition);
        }
    }

    @Override
    public void onUserArticleItemClickListener(int which, ShareArticleJson data, int itemPosition) {
        switch (which) {
            case DELETE:
                mView.showConfirmDeleteDialog(data,itemPosition);
                break;
            case EDIT:
                mView.showEditDialog(data,itemPosition);
                break;
            default:
                break;
        }
    }

    @Override
    public void onDeleteArticleConfirm(ShareArticleJson data, int itemPosition) {
        mView.deleteArticle(data,itemPosition);
    }

    @Override
    public void onEditButtonClickListener(ShareArticleJson data, int itemPosition, String content) {
        data.setContent(content);
        String jsonStr = gson.toJson(data);
        mView.updateFirebase(jsonStr,itemPosition,content,data.getOldContent());
    }

    @Override
    public void onCatchNoData() {
        mView.showProgress(false);
        mView.showNoDataView(true);
    }

    @Override
    public void onStrangerItemClickListener(int which, ShareArticleJson data, int itemPosition) {
        switch (itemPosition){
            case IMPEACHMENT:
                ArrayList<String> dialogList = new ArrayList<>();
                dialogList.add(mView.getTrushArticle());
                dialogList.add(mView.getNotGoodMessage());
                mView.showImpeachmentDialog(dialogList,data);
                break;
        }
    }

    @Override
    public void onImpeachmentItemClickListener(ArrayList<String> dialogList, int type, ShareArticleJson data) {
        String emailBody = String.format(Locale.getDefault(),"文章 : %s 內文 : %s \n檢舉內容 : %s\n還有其他想說的可以打在下方(檢舉文若屬實,我會立即處理請放心,處理完會在回信給您. 請耐心等候:\n",data.getOldContent(),data.getContent(),dialogList.get(type));
        mView.sendEmailToCreator(emailBody);
    }
}
