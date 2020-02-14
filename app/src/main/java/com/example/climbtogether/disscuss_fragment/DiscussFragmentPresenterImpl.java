package com.example.climbtogether.disscuss_fragment;

import java.util.ArrayList;

public class DiscussFragmentPresenterImpl implements DiscussFragmentPresenter {

    private DiscussFragmentVu mView;

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
        mView.intentToChatActivity(listName);
    }
}
