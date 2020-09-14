package com.hiking.climbtogether.home_activity;

import com.android.billingclient.api.SkuDetails;

import java.util.List;

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

    void OnPrintOrderList(List<SkuDetails> list);

    void onDonateError(String errorMessage);
}
