package com.hiking.climbtogether.vote_activity.vote_view_holder;

import java.util.ArrayList;

public interface VoteViewHolderPresenter {
    void onSpinnerItemClickListener();

    void onAlertDialogItemClickListener(String[] numberArray, int which);

    void onCreateVoteBtnClickListener(ArrayList<String> itemTextArray, String time, String title);

    void onIvDateClickListener();
}
