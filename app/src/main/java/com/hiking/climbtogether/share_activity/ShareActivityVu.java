package com.hiking.climbtogether.share_activity;

import com.hiking.climbtogether.share_activity.share_json.ShareArticleJson;
import com.hiking.climbtogether.tool.UserDataManager;

import java.util.ArrayList;

public interface ShareActivityVu {
    void closePage();

    void onShowAddArticleDialog();

    void showErrorMessage(String message);

    void showProgress(boolean isShow);

    void showReplayDialog(ShareArticleJson shareArticleDTO);

    void sendReply(String content, ArrayList<ReplyDTO> replyArray, ShareArticleJson shareArticleDTO);

    void searchForFriendship(ShareArticleJson data);

    void showUserDialog(ShareArticleJson data, boolean isInvite, boolean isFriend);

    void showUserDialog();

    void setProgressStart(boolean b);

    void sendInviteToStranger(String strangerEmail, String userEmail);

    void checkFriendship(ShareArticleJson data);

    void intentToPersonalChatActivity(ShareArticleJson data);

    void showNoticeDialog(ShareArticleJson data);

    void setNewRecyclerView(ArrayList<ShareArticleJson> dataArrayList);

    void shareArticleJson(String jsonStr, String content, long currentTime);

    void showProgressMessage(String message);

    void uploadPhoto(UserDataManager userDataManager, String content, ArrayList<byte[]> photoBytesArray);

    String getUserEmail();

    String getDeleteStr();

    String getEditStr();

    void showUserArticleDialog(ArrayList<String> dialogList, ShareArticleJson data, int itemPosition);

    void showConfirmDeleteDialog(ShareArticleJson data, int itemPosition);

    void deleteArticle(ShareArticleJson data, int itemPosition);

    void showEditDialog(ShareArticleJson data, int itemPosition);

    void updateFirebase(String jsonStr, int itemPosition, String newContent, String oldContent);

    void showNoDataView(boolean isShow);

    String getImpeachment();

    void showStrangerArticleDialog(ArrayList<String> dialogList, ShareArticleJson data, int itemPosition);

    void showImpeachmentDialog(ArrayList<String> dialogList, ShareArticleJson data);

    String getTrushArticle();

    String getNotGoodMessage();

    void sendEmailToCreator(String emailBody);
}
