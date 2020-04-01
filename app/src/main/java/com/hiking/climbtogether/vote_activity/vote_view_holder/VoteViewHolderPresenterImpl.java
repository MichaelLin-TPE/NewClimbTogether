package com.hiking.climbtogether.vote_activity.vote_view_holder;

import android.util.Log;

import com.google.gson.Gson;

import java.util.ArrayList;

public class VoteViewHolderPresenterImpl implements VoteViewHolderPresenter {

    private VoteViewHolderVu mView;

    private Gson gson;

    public VoteViewHolderPresenterImpl(VoteViewHolderVu mView) {
        this.mView = mView;
        gson = new Gson();
    }

    @Override
    public void onSpinnerItemClickListener() {
        mView.showNumberSelectDialog();
    }

    @Override
    public void onAlertDialogItemClickListener(String[] numberArray, int which) {
        mView.setTvSpinnerText(numberArray[which]);
        mView.addEditTextView(numberArray[which]);
    }

    @Override
    public void onCreateVoteBtnClickListener(ArrayList<String> itemTextArray, String time, String title) {

        if (itemTextArray.size() == 0){
            String message = "請填入投票項目";
            mView.showErrorCode(message);
            return;
        }
        if (time == null || time.isEmpty()){
            String message = "請選擇日期";
            mView.showErrorCode(message);
            return;
        }
        if (title == null || title.isEmpty()){
            String message = "請輸入標題";
            mView.showErrorCode(message);
            return;
        }
        VoteData data = new VoteData();
        data.setTitle(title);
        data.setTime(time);
        data.setItemArray(itemTextArray);
        data.setCreator(mView.getUserEmail());
        data.setVoteAlreadyArray(null);
        String jsonStr = gson.toJson(data);
        mView.saveVoteDataToFirebase(jsonStr,title);

    }

    @Override
    public void onIvDateClickListener() {
        mView.showDatePicker();
    }
}
