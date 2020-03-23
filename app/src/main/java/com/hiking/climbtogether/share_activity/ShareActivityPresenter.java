package com.hiking.climbtogether.share_activity;

import com.hiking.climbtogether.share_activity.share_json.ShareArticleJson;
import com.hiking.climbtogether.tool.UserDataManager;

import java.util.ArrayList;

public interface ShareActivityPresenter {
    void onBackButtonClick();

    void onAddArticleClick();

    void onShareButtonClick(UserDataManager userDataManager, String content, ArrayList<byte[]> photoBytesArray);

    void onShowSuccessShareArticle();

    void onShowProgress();

    void onCloseProgress();

    void onReplyButtonClick(ShareArticleJson shareArticleDTO);

    void onButtonSendReplyClick(ArrayList<ReplyDTO> replyArray, String content, ShareArticleJson shareArticleDTO);

    void onUserPhotoClickListener(ShareArticleJson data);

    void onShowUserDialog(ShareArticleJson data, boolean isInvite, boolean isFriend);

    void onSetDialogViewChange();

    void onSearchFriendShip();

    void onAddFriendButtonClickListener(String strangerEmail, String userEmail);

    void onSendMessageClickListener(ShareArticleJson data);

    void onIsFriend(ShareArticleJson data, boolean isFriendSend);

    void onCatchAllJson(ArrayList<String> jsonStrArray);

    void onCatchallPhotoUrl(UserDataManager userDataManager,String content,ArrayList<String> photoUrlArray);

    void onSettingButtonClickListener(ShareArticleJson data, int itemPosition);

    void onUserArticleItemClickListener(int which, ShareArticleJson data, int itemPosition);

    void onDeleteArticleConfirm(ShareArticleJson data, int itemPosition);

    void onEditButtonClickListener(ShareArticleJson data, int itemPosition, String content);

    void onCatchNoData();

    void onStrangerItemClickListener(int which,ShareArticleJson data,int itemPosition);

    void onImpeachmentItemClickListener(ArrayList<String> dialogList, int type, ShareArticleJson data);
}
