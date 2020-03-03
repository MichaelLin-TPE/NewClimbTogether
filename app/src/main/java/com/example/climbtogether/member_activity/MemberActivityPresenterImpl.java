package com.example.climbtogether.member_activity;

import com.example.climbtogether.R;

import java.util.ArrayList;

import static com.example.climbtogether.tool.Constant.APPLY_MT;
import static com.example.climbtogether.tool.Constant.CENTER_WEATHER;
import static com.example.climbtogether.tool.Constant.EQUIPMENT;
import static com.example.climbtogether.tool.Constant.FRIEND_MANAGER;
import static com.example.climbtogether.tool.Constant.SEARCH_BED;
import static com.example.climbtogether.tool.Constant.TOP_PEAK;

public class MemberActivityPresenterImpl implements MemberActivityPresenter {

    private MemberActivityVu mView;


    public MemberActivityPresenterImpl(MemberActivityVu mView) {
        this.mView = mView;
    }

    @Override
    public void onShowRecycler() {
        ArrayList<String> btnList = new ArrayList<>();
        btnList.add(mView.getVuContext().getString(R.string.favorite_mt));
        btnList.add(mView.getVuContext().getString(R.string.search_bed));
        btnList.add(mView.getVuContext().getString(R.string.apply_mt));
        btnList.add(mView.getVuContext().getString(R.string.center_weather));
        btnList.add(mView.getVuContext().getString(R.string.friend_manager));
        btnList.add(mView.getVuContext().getString(R.string.equipment_list));

        mView.setRecyclerView(btnList);


    }

    @Override
    public void onChangeView(boolean isShow) {
        mView.downloadUserPhoto();
        mView.changeView(isShow);
    }

    @Override
    public void onLoginButtonClickListener() {

        mView.intentToLoginActivity();
    }

    @Override
    public void onSignOutClickListener() {
        mView.showConfirmSignOutDialog();


    }

    @Override
    public void onConfirmSignOutClickListener() {
        mView.signOut();
    }

    @Override
    public void onListItemClickListener(int itemPosition) {
        String url;
        switch (itemPosition) {
            case TOP_PEAK:
                mView.intentToMtCollectionActivity();
                break;
            case SEARCH_BED:
                url = "https://npm.cpami.gov.tw/bed_menu.aspx";
                mView.intentToBrowser(url);
                break;
            case APPLY_MT:
                url = "https://npm.cpami.gov.tw/apply_1.aspx";
                mView.intentToBrowser(url);
                break;
            case CENTER_WEATHER:
                url = "https://www.cwb.gov.tw/V8/C/";
                mView.intentToBrowser(url);
                break;
            case FRIEND_MANAGER:
                mView.intentToFriendManagerActivity();
                break;
            case EQUIPMENT:
                mView.intentToMyEquipmentActivity();

                break;
            default:
                break;
        }
    }

    @Override
    public void onUploadUserPhotoListener() {
        mView.uploadUserPhoto();
    }

    @Override
    public void onShowProgressToast(String message) {
        mView.showToast(message);
    }
}
