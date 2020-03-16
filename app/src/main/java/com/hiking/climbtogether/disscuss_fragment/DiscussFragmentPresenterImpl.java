package com.hiking.climbtogether.disscuss_fragment;

import com.hiking.climbtogether.R;

import java.util.ArrayList;

public class DiscussFragmentPresenterImpl implements DiscussFragmentPresenter {

    private DiscussFragmentVu mView;

    private static final String DISCUSS = "登山\n即時討論區";

    private static final String SHARE = "登山\n動態分享";

    public DiscussFragmentPresenterImpl(DiscussFragmentVu mView){
        this.mView = mView;
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
    public void onClearView() {
        mView.setViewMaintain(false);
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

    @Override
    public void onPrepareData() {
        ArrayList<String> listNameArray = new ArrayList<>();
        listNameArray.add(mView.getVuContext().getString(R.string.discuss_chat));
        listNameArray.add(mView.getVuContext().getString(R.string.share_news));
        mView.showProgressbar(false);
        mView.setRecyclerView(listNameArray);
    }
}
