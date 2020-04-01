package com.hiking.climbtogether.vote_activity;

public class VoteActivityPresenterImpl implements VoteActivityPresenter {

    private VoteActivityVu mView;

    public VoteActivityPresenterImpl(VoteActivityVu mView) {
        this.mView = mView;
    }

    @Override
    public void onBackButtonClickListener() {
        mView.closePage();
    }

    @Override
    public void onShowView() {
        mView.setRecyclerView();
    }
}
