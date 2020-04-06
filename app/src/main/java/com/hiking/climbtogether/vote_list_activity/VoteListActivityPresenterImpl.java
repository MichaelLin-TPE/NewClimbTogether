package com.hiking.climbtogether.vote_list_activity;

import android.util.Log;

import com.google.gson.Gson;
import com.hiking.climbtogether.vote_activity.vote_view_holder.VoteData;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;

public class VoteListActivityPresenterImpl implements VoteListActivityPresenter {

    private VoteListActivityVu mView;

    private Gson gson;

    private ArrayList<VoteData> votingArrayList;

    private ArrayList<VoteData> voteDayLineArray;

    public VoteListActivityPresenterImpl(VoteListActivityVu mView) {
        this.mView = mView;
        gson = new Gson();
    }

    @Override
    public void onBackButtonClickListener() {
        mView.closePage();
    }

    @Override
    public void onCatchJsonStr(ArrayList<String> jsonArray, ArrayList<Boolean> isDayLineArray) {
        if (jsonArray.size() != 0) {
            ArrayList<VoteData> catchDataArray = new ArrayList<>();

            voteDayLineArray = new ArrayList<>();
            votingArrayList = new ArrayList<>();

            for (String json : jsonArray) {
                VoteData data = gson.fromJson(json, VoteData.class);
                catchDataArray.add(data);
            }

            //拆資料
            for (int i = 0; i < isDayLineArray.size(); i++) {
                if (isDayLineArray.get(i)) {
                    voteDayLineArray.add(catchDataArray.get(i));
                } else {
                    votingArrayList.add(catchDataArray.get(i));
                }
            }

            Log.i("Michael", "投票中有幾筆 : " + votingArrayList.size() + " , 已結束有幾筆 : " + voteDayLineArray.size());

            //先判斷時間是否到期
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd", Locale.TAIWAN);
            long currentTime = System.currentTimeMillis();

            String currentDate = sdf.format(new Date(currentTime));

            Iterator<VoteData> iterator = votingArrayList.iterator();
            int index = 0;
            while (iterator.hasNext()) {
                String dateTime = iterator.next().getTime();
                try {
                    SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd", Locale.TAIWAN);

                    Date myTime = format.parse(currentDate);
                    Date voteTime = format.parse(dateTime);

                    Log.i("Michael", "目前時間 : " + myTime + " , 投票截止日 : " + voteTime);

                    if (myTime != null && voteTime != null) {
                        if (myTime.after(voteTime)) {
                            //新增投票截止
                            if (index < votingArrayList.size()){
                                voteDayLineArray.add(votingArrayList.get(index));
                                //刪除投票中
                                iterator.remove();
                            }
                        }
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                index ++;
            }

            //判斷完時間,要把時間過得 is_day_line改成 true
            mView.updateVoteDayLineData(voteDayLineArray);


            //判斷是否有投過票
            boolean isVote = false;
            ArrayList<Boolean> isVoteArray = new ArrayList<>();

            for (int i = 0; i < votingArrayList.size(); i++) {
                if (votingArrayList.get(i).getVoteAlreadyArray() != null) {
                    for (int j = 0; j < votingArrayList.get(i).getVoteAlreadyArray().size(); j++) {
                        if (votingArrayList.get(i).getVoteAlreadyArray().get(j).equals(mView.getUserEmail())) {
                            isVote = true;
                            break;
                        } else {
                            isVote = false;
                        }
                    }
                } else {
                    isVote = false;
                }
                isVoteArray.add(isVote);
            }


            if (votingArrayList.size() != 0 || voteDayLineArray.size() != 0) {
                mView.setRecyclerView(votingArrayList, isVoteArray, voteDayLineArray);
            } else {
                Log.i("Michael", "votingArrayList null");
            }
        } else {
            Log.i("Michael", "沒資料");
        }
    }

    @Override
    public void onVotingItemClickListener(VoteData data, int itemPosition) {
        mView.showVotingDialog(data, itemPosition);
    }

    @Override
    public void onBtnDecideClickListener(String itemContent, VoteData data, int itemPosition) {
        if (itemContent.isEmpty()) {
            String message = "麻煩投票...";
            mView.showErrorCode(message);
            return;
        }
        if (votingArrayList.get(itemPosition).getVoteAlreadyArray() != null) {
            votingArrayList.get(itemPosition).getVoteAlreadyArray().add(mView.getUserEmail());

            String jsonStr = gson.toJson(votingArrayList.get(itemPosition));

            mView.updateVoteDataToFirebase(votingArrayList.get(itemPosition).getTitle(), jsonStr);

        } else {
            VoteData voteData = votingArrayList.get(itemPosition);
            ArrayList<String> voteMemberArray = new ArrayList<>();
            voteMemberArray.add(mView.getUserEmail());
            voteData.setVoteAlreadyArray(voteMemberArray);
            String jsontStr = gson.toJson(voteData);
            mView.updateVoteDataToFirebase(voteData.getTitle(), jsontStr);
        }


        mView.saveVoteDataToFirebase(itemContent, data);
    }

    @Override
    public void onVoteAlreadyItemClickListener(VoteData data) {
        mView.showResultDialog(data);
    }
}
