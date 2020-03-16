package com.hiking.climbtogether.member_activity;

public interface MemberActivityPresenter {
    void onShowRecycler();

    void onChangeView(boolean isShow, String displayName);

    void onLoginButtonClickListener();

    void onSignOutClickListener();

    void onConfirmSignOutClickListener();

    void onListItemClickListener(int itemPosition);

    void onUploadUserPhotoListener();

    void onShowProgressToast(String message);
}
