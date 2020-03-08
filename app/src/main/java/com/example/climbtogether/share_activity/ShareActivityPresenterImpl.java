package com.example.climbtogether.share_activity;

import android.util.Log;

import com.example.climbtogether.tool.UserDataManager;

import java.util.ArrayList;

public class ShareActivityPresenterImpl implements ShareActivityPresenter {

    private ShareActivityVu mView;

    private ShareArticleDTO data;

    public ShareActivityPresenterImpl(ShareActivityVu mView) {
        this.mView = mView;
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
        mView.shareArticle(userDataManager,content,photoBytesArray);
    }

    @Override
    public void onCatchSelectPhotoUrl(ArrayList<String> downloadUrlArray,String content) {
        mView.createArticle(downloadUrlArray,content);
    }

    @Override
    public void onShowSuccessShareArticle() {
        String message = "分享成功";
        mView.showErrorMessage(message);
    }

    @Override
    public void onCatchAllData(ArrayList<ShareArticleDTO> shareArray, ArrayList<LikeMemberDTO> listMemberArray) {
        mView.setRecyclerView(shareArray,listMemberArray);
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
    public void onReplyButtonClick(ShareArticleDTO shareArticleDTO) {
        mView.showReplayDialog(shareArticleDTO);
    }

    @Override
    public void onButtonSendReplyClick(ArrayList<ReplyDTO> replyArray, String content, ShareArticleDTO shareArticleDTO) {
        if (content == null || content.isEmpty()){
            String message = "留下些甚麼吧...";
            mView.showErrorMessage(message);
            return;
        }
        mView.sendReply(content,replyArray,shareArticleDTO);


    }

    @Override
    public void onUserPhotoClickListener(ShareArticleDTO data) {
        this.data = data;
        mView.showUserDialog();

    }

    @Override
    public void onShowUserDialog(ShareArticleDTO data, boolean isInvite, boolean isFriend) {
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
    public void onSendMessageClickListener(ShareArticleDTO data) {
        mView.checkFriendship(data);
    }

    @Override
    public void onIsFriend(ShareArticleDTO data,boolean isFriendSend) {
        this.data = data;
        if (isFriendSend){
            Log.i("Michael","是朋友");
            mView.intentToPersonalChatActivity(data);
        }else {
            Log.i("Michael","不是朋友");
            mView.showNoticeDialog(data);
        }
    }
}
