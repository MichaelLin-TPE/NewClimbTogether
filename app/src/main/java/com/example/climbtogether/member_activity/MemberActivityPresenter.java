package com.example.climbtogether.member_activity;

public interface MemberActivityPresenter {
    void onShowRecycler();

    void onChangeView(boolean isShow);

    void onLoginButtonClickListener();

    void onSignOutClickListener();

    void onConfirmSignOutClickListener();

    void onListItemClickListener(int itemPosition);
}
