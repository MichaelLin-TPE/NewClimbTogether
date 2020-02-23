package com.example.climbtogether.disscuss_fragment;

import java.util.ArrayList;

public class DiscussFragmentPresenterImpl implements DiscussFragmentPresenter {

    private DiscussFragmentVu mView;

    private static final String DISCUSS = "登山即時討論區";

    private static final String SHARE = "登山心得分享";

    public DiscussFragmentPresenterImpl(DiscussFragmentVu mView){
        this.mView = mView;
    }

    @Override
    public void onSearchFirestoreData() {
        mView.searchFirestoreData();
    }

    @Override
    public void onNotLoginEvent() {
        mView.setViewMaintain(true);
    }

    @Override
    public void onBtnLoginClickListener() {
        mView.intentToLoginActivity();
    }

    @Override
    public void onCatchDataSuccessful(ArrayList<String> listArrayList) {
        mView.showProgressbar(false);
        mView.setRecyclerView(listArrayList);
    }

    @Override
    public void onShowProgressbar() {
        mView.showProgressbar(true);
        mView.setViewMaintain(false);
    }

    @Override
    public void onClearView() {
        mView.clearView();
    }

    @Override
    public void onDiscussItemClickListener(String listName) {
        switch (listName){
            case DISCUSS:
                mView.intentToChatActivity(listName);
                break;
            case SHARE:
                mView.intentToShareActivity(listName);
                break;
        }

    }
}
