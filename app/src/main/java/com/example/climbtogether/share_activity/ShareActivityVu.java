package com.example.climbtogether.share_activity;

import com.example.climbtogether.tool.UserDataManager;

import java.util.ArrayList;

public interface ShareActivityVu {
    void closePage();

    void onShowAddArticleDialog();

    void showErrorMessage(String message);

    void shareArticle(UserDataManager userDataManager, String content, ArrayList<byte[]> photoBytesArray);

    void createArticle(ArrayList<String> downloadUrlArray, String content);

    void setRecyclerView(ArrayList<ShareArticleDTO> shareArray,ArrayList<LikeMemberDTO> listMemberArray,ArrayList<ReplyObject> replyArray);

    void showProgress(boolean isShow);

    void showReplayDialog(ReplyObject data,ShareArticleDTO shareArticleDTO);

    void sendReply(String content, ArrayList<ReplyDTO> replyArray, ShareArticleDTO shareArticleDTO);
}
