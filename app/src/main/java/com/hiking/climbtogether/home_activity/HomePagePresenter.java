package com.hiking.climbtogether.home_activity;

public interface HomePagePresenter {
    void onPrepareData();

    void onPrepareViewPager();

    void onMemberIconClickListener();

    void onQuestionButtonClickListener();

    void onContactMeButtonClickListener();

    void onDonateClickListener();

    void onSelectItemClickListener(int which);

    void onCheckGoogleUpdateVersion();

    void onShowUpdateDialog();

    void onUpdateConfirmClickListener();
}
