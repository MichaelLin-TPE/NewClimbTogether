package com.example.climbtogether.share_activity;

import com.example.climbtogether.tool.UserDataManager;

import java.util.ArrayList;

public interface ShareActivityPresenter {
    void onBackButtonClick();

    void onAddArticleClick();

    void onShareButtonClick(UserDataManager userDataManager, String content, ArrayList<byte[]> photoBytesArray);

    void onCatchSelectPhotoUrl(ArrayList<String> downloadUrlArray,String content);

    void onShowSuccessShareArticle();

    void onCatchAllData(ArrayList<ShareArticleDTO> shareArray,ArrayList<LikeMemberDTO> listMemberArray,ArrayList<ReplyObject> replyArray);

    void onShowProgress();

    void onCloseProgress();

    void onReplyButtonClick(ReplyObject data,ShareArticleDTO shareArticleDTO);

    void onButtonSendReplyClick(ArrayList<ReplyDTO> replyArray, String content, ShareArticleDTO shareArticleDTO);

    void onUserPhotoClickListener(ShareArticleDTO data);

    void onShowUserDialog(ShareArticleDTO data, boolean isInvite, boolean isFriend);

    void onSetDialogViewChange();

    void onSearchFriendShip();

    void onAddFriendButtonClickListener(String strangerEmail, String userEmail);

    void onSendMessageClickListener(ShareArticleDTO data);

    void onIsFriend(ShareArticleDTO data,boolean isFriendSend);
}
