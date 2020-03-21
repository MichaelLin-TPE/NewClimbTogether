package com.hiking.climbtogether.home_activity;

import android.graphics.drawable.Drawable;

import com.hiking.climbtogether.R;

import java.util.ArrayList;

public class HomePagePresenterImpl implements HomePagePresenter {

    private HomePageVu mView;

    public HomePagePresenterImpl(HomePageVu mView){
        this.mView = mView;
    }

    @Override
    public void onPrepareData() {
        ArrayList<String> tabTitleArray = new ArrayList<>();
        tabTitleArray.add(mView.getVuContext().getResources().getString(R.string.home));
        tabTitleArray.add(mView.getVuContext().getResources().getString(R.string.mountain));
        tabTitleArray.add(mView.getVuContext().getResources().getString(R.string.equipment));
        tabTitleArray.add(mView.getVuContext().getResources().getString(R.string.discuss));
        tabTitleArray.add(mView.getVuContext().getString(R.string.personal_chat));
        /**
         * 未點擊
         */
        ArrayList<Drawable> notPressIconArray = new ArrayList<>();
        notPressIconArray.add(mView.getVuContext().getResources().getDrawable(R.drawable.home_not_press));
        notPressIconArray.add(mView.getVuContext().getResources().getDrawable(R.drawable.mt_not_press));
        notPressIconArray.add(mView.getVuContext().getResources().getDrawable(R.drawable.equipment_not_press));
        notPressIconArray.add(mView.getVuContext().getResources().getDrawable(R.drawable.chat_not_press));
        notPressIconArray.add(mView.getVuContext().getResources().getDrawable(R.drawable.personal_chat_not_pressed));

        /**
         * 點擊後
         */
        ArrayList<Drawable> pressedIconArray = new ArrayList<>();
        pressedIconArray.add(mView.getVuContext().getResources().getDrawable(R.drawable.home_pressed));
        pressedIconArray.add(mView.getVuContext().getResources().getDrawable(R.drawable.mt_pressed));
        pressedIconArray.add(mView.getVuContext().getResources().getDrawable(R.drawable.equipment_pressed));
        pressedIconArray.add(mView.getVuContext().getResources().getDrawable(R.drawable.chat_pressed));
        pressedIconArray.add(mView.getVuContext().getResources().getDrawable(R.drawable.personal_chat_pressed));

        mView.showBottomTabLayout(tabTitleArray,notPressIconArray,pressedIconArray);
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
}
