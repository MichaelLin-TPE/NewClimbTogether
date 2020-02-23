package com.example.climbtogether.share_activity;

import com.example.climbtogether.tool.UserDataManager;

import java.util.ArrayList;

public interface ShareActivityPresenter {
    void onBackButtonClick();

    void onAddArticleClick();

    void onShareButtonClick(UserDataManager userDataManager, String content, byte[] selectPhotoBytes);

    void onCatchSelectPhotoUrl(String url,String content);

    void onShowSuccessShareArticle();

    void onCatchAllData(ArrayList<ShareArticleDTO> shareArray,ArrayList<LikeMemberDTO> listMemberArray);
}
