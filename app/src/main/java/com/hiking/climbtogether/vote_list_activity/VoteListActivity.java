package com.hiking.climbtogether.vote_list_activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.hiking.climbtogether.R;
import com.hiking.climbtogether.vote_activity.vote_view_holder.VoteData;
import com.hiking.climbtogether.vote_list_activity.list_view_presenter.ListViewPresenter;
import com.hiking.climbtogether.vote_list_activity.list_view_presenter.ListViewPresenterImpl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class VoteListActivity extends AppCompatActivity implements VoteListActivityVu {

    private VoteListActivityPresenter presenter;

    private FirebaseAuth mAuth;

    private FirebaseFirestore firestore;

    private FirebaseUser user;

    private RecyclerView recyclerView;

    private ListViewPresenter listViewPresenter;

    private String itemContent = "";

    private ArrayList<Boolean> isVoteArray;

    private static final String VOTE = "vote";

    private static final String VOTE_FORBID = "vote_forbid";

    private ArrayList<String> voteContentArray;

    private ArrayList<Integer> voteNumberArray;

    private int voteCount;

    private String voteTitle,voteCreator;

    private TextView tvTitle ;
    private TextView tvCreator;
    private RecyclerView resultRecyclerView;

    private ProgressBar resultProgress;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vote_list);
        initPresenter();
        initFirebase();
        initView();
        searchData();
    }

    private void searchData() {
        firestore.collection(VOTE)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful() && task.getResult() != null){
                            ArrayList<String> jsonArray = new ArrayList<>();
                            ArrayList<Boolean> isDayLineArray = new ArrayList<>();
                            for (QueryDocumentSnapshot snapshot : task.getResult()){
                                jsonArray.add((String) snapshot.get("json"));
                                isDayLineArray.add((boolean)snapshot.get("is_day_line"));
                            }
                            presenter.onCatchJsonStr(jsonArray,isDayLineArray);
                        }
                    }
                });
    }

    private void initView() {
        Toolbar toolbar = findViewById(R.id.vote_list_toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.onBackButtonClickListener();
            }
        });

        recyclerView = findViewById(R.id.vote_list_recycler_view);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

    }

    private void initFirebase() {
        firestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
    }

    private void initPresenter() {
        listViewPresenter = new ListViewPresenterImpl();
        presenter = new VoteListActivityPresenterImpl(this);
    }

    @Override
    public void closePage() {
        finish();
    }

    @Override
    public void setRecyclerView(ArrayList<VoteData> dataArrayList, ArrayList<Boolean> isVoteArray, ArrayList<VoteData> voteDayLineArray) {
        this.isVoteArray = isVoteArray;
        listViewPresenter.setData(dataArrayList);
        listViewPresenter.setVoteDayLineData(voteDayLineArray);
        VoteListAdapter adapter = new VoteListAdapter(this,listViewPresenter);
        recyclerView.setAdapter(adapter);
        adapter.setOnVotingItemClickListener(new VotingAdapter.OnVotingItemClickListener() {
            @Override
            public void onClick(VoteData data,int itemPosition) {
                presenter.onVotingItemClickListener(data,itemPosition);
            }
        });
        adapter.setOnVoteAlreadyItemClickListener(new VoteAlreadyAdapter.OnVoteAlreadyItemClickListener() {
            @Override
            public void onClick(VoteData data) {
                presenter.onVoteAlreadyItemClickListener(data);
            }
        });
    }

    @Override
    public void showVotingDialog(VoteData data, int itemPosition) {
        View view = View.inflate(this,R.layout.voting_dialog_view,null);
        TextView tvTitle = view.findViewById(R.id.voting_dialog_title);
        TextView tvCreator = view.findViewById(R.id.voting_dialog_creator);
        RecyclerView dialogRecyclerView = view.findViewById(R.id.voting_dialog_recycler_view);
        Button btnDecide = view.findViewById(R.id.voting_dialog_btn_decide);

        if (isVoteArray.get(itemPosition)){
            btnDecide.setText(getString(R.string.vote_already));
            btnDecide.setTextColor(ContextCompat.getColor(this,R.color.grey));
            btnDecide.setEnabled(false);
        }else {
            btnDecide.setText(getString(R.string.i_am_ready));
            btnDecide.setTextColor(ContextCompat.getColor(this,R.color.colorPrimaryDark));
            btnDecide.setEnabled(true);
        }

        dialogRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        VotingDialogAdapter dialogAdapter = new VotingDialogAdapter(data.getItemArray(),this);
        dialogRecyclerView.setAdapter(dialogAdapter);

        tvTitle.setText(data.getTitle());

        tvCreator.setText(String.format(Locale.getDefault(),"發起人 : %s\n投票截止日 : %s",data.getCreator(),data.getTime()));

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setView(view).create();
        Window window = dialog.getWindow();
        if (window != null){
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
        dialog.show();

        dialogAdapter.setOnVotingDialogItemClickListener(new VotingDialogAdapter.OnVotingDialogItemClickListener() {
            @Override
            public void onClick(String content) {
                itemContent = content;
                Log.i("Michael","投了 : "+content);
                dialogRecyclerView.post(new Runnable() {
                    @Override
                    public void run() {
                        dialogAdapter.notifyDataSetChanged();
                    }
                });
            }
        });
        btnDecide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                presenter.onBtnDecideClickListener(itemContent,data,itemPosition);
            }
        });
    }

    @Override
    public void showErrorCode(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void saveVoteDataToFirebase(String itemContent, VoteData data) {
        Map<String,Object> map = new HashMap<>();
        if (user != null && user.getEmail() != null){
            firestore.collection("vote")
                    .document(data.getTitle())
                    .collection(itemContent)
                    .document(user.getEmail())
                    .set(map)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                showErrorCode(getString(R.string.vote_successful));
                            }
                        }
                    });
        }


    }

    @Override
    public String getUserEmail() {
        return user.getEmail();
    }

    @Override
    public void updateVoteDataToFirebase(String title, String jsonStr) {
        Map<String,Object> map = new HashMap<>();
        map.put("json",jsonStr);
        firestore.collection("vote")
                .document(title)
                .set(map, SetOptions.merge());
        searchData();
    }

    @Override
    public void updateVoteDayLineData(ArrayList<VoteData> voteDayLineArray) {
        for (VoteData data : voteDayLineArray){
            Map<String,Object> map = new HashMap<>();
            map.put("is_day_line",true);
            firestore.collection(VOTE)
                    .document(data.getTitle())
                    .set(map,SetOptions.merge());
        }
    }

    @Override
    public void showResultDialog(VoteData data) {
        voteTitle = data.getTitle();
        voteCreator = data.getCreator();
        voteContentArray = data.getItemArray();
        voteNumberArray = new ArrayList<>();
        voteCount = 0;
        View view = View.inflate(this,R.layout.vote_result_dialog,null);
        tvTitle = view.findViewById(R.id.vote_result_title);
        tvCreator = view.findViewById(R.id.vote_result_creator);
        resultRecyclerView = view.findViewById(R.id.vote_result_recycler_view);
        resultProgress = view.findViewById(R.id.vote_result_progressbar);

        resultProgress.setVisibility(View.VISIBLE);


        resultRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setView(view).create();
        Window window = dialog.getWindow();
        if (window != null){
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }

        dialog.show();
        searchVoteNumber();


    }

    private void searchVoteNumber() {
        if (voteCount < voteContentArray.size()){
            Log.i("Michael","投票項目 : "+voteContentArray.get(voteCount));
            firestore.collection(VOTE)
                    .document(voteTitle)
                    .collection(voteContentArray.get(voteCount))
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful() && task.getResult() != null){
                                int voteNumber = 0;
                                for (QueryDocumentSnapshot snapshot : task.getResult()){
                                    voteNumber ++;
                                }
                                Log.i("Michael","投票次數 : "+voteNumber);
                                voteNumberArray.add(voteNumber);
                                voteCount ++;
                                searchVoteNumber();
                            }
                        }
                    });
        }else {
            resultProgress.setVisibility(View.GONE);
            ArrayList<VoteResultData> dataArrayList = new ArrayList<>();
            for (int i = 0 ; i < voteContentArray.size() ; i++){
                VoteResultData data = new VoteResultData();
                data.setContent(voteContentArray.get(i));
                data.setNumber(voteNumberArray.get(i));
                dataArrayList.add(data);
            }

            tvTitle.setText(voteTitle);

            tvCreator.setText(String.format(Locale.getDefault(),"發起人 : %s",voteCreator));

            VoteResultAdapter resultAdapter = new VoteResultAdapter(this,dataArrayList);
            resultRecyclerView.setAdapter(resultAdapter);


        }
    }
}
