package com.example.climbtogether.share_activity;

import com.example.climbtogether.tool.UserDataManager;

import java.util.ArrayList;

public class ShareActivityPresenterImpl implements ShareActivityPresenter {

    private ShareActivityVu mView;

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
    public void onShareButtonClick(UserDataManager userDataManager, String content, byte[] selectPhotoBytes) {
        if (content == null || content.isEmpty()){
            String message = "請輸入內容";
            mView.showErrorMessage(message);
            return;
        }
        mView.shareArticle(userDataManager,content,selectPhotoBytes);
    }

    @Override
    public void onCatchSelectPhotoUrl(String selectPhotoUrl,String content) {
        mView.createArticle(selectPhotoUrl,content);
    }

    @Override
    public void onShowSuccessShareArticle() {
        String message = "分享成功";
        mView.showErrorMessage(message);
    }

    @Override
    public void onCatchAllData(ArrayList<ShareArticleDTO> shareArray,ArrayList<LikeMemberDTO> listMemberArray,ArrayList<ReplyObject> replyArray) {
        mView.setRecyclerView(shareArray,listMemberArray,replyArray);
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
    public void onReplyButtonClick(ReplyObject data,ShareArticleDTO shareArticleDTO) {
        mView.showReplayDialog(data,shareArticleDTO);
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
}
