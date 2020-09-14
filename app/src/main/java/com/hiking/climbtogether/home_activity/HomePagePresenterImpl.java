package com.hiking.climbtogether.home_activity;

import android.graphics.drawable.Drawable;

import com.android.billingclient.api.SkuDetails;
import com.hiking.climbtogether.tool.DataProvider;

import java.util.ArrayList;
import java.util.List;

public class HomePagePresenterImpl implements HomePagePresenter {

    private HomePageVu mView;

    private static final int SINGLE_BUY = 0;

    private static final int LOOP_BUY = 1;

    public HomePagePresenterImpl(HomePageVu mView) {
        this.mView = mView;
    }

    @Override
    public void onPrepareData() {
        ArrayList<String> tabTitleArray = DataProvider.getInstance().getTabTitleArray();
        /**
         * 未點擊
         */
        ArrayList<Drawable> notPressIconArray = DataProvider.getInstance().getNotPressIconArray();

        /**
         * 點擊後
         */
        ArrayList<Drawable> pressedIconArray = DataProvider.getInstance().getPressedIconArray();

        mView.showBottomTabLayout(tabTitleArray, notPressIconArray, pressedIconArray);
    }

    @Override
    public void onPrepareViewPager() {
        mView.showViewPager();
    }

    @Override
    public void onMemberIconClickListener() {
        mView.intentToMemberActivity();
    }

    @Override
    public void onQuestionButtonClickListener() {
        mView.showQuestionDialog();
    }

    @Override
    public void onContactMeButtonClickListener() {
        mView.contactMe();
    }

    @Override
    public void onDonateClickListener() {
        mView.checkGooglePlayAccount();
    }


    @Override
    public void onSelectItemClickListener(int which) {
        switch (which) {
            case SINGLE_BUY:
                mView.showSingleDonateDialog();
                break;
            case LOOP_BUY:
                mView.showLoopDonateDialog();
                break;
            default:
                break;
        }
    }

    @Override
    public void onCheckGoogleUpdateVersion() {
        mView.checkGoogleUpdate();
    }

    @Override
    public void onShowUpdateDialog() {
        mView.showUpdateDialog();
    }

    @Override
    public void onUpdateConfirmClickListener() {
        mView.intentToGooglePlay();
    }

    @Override
    public void OnPrintOrderList(List<SkuDetails> list) {
        mView.printOrderList(list);
    }

    @Override
    public void onDonateError(String errorMessage) {
        mView.showToast(errorMessage);
    }
}
