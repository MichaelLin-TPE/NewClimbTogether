package com.hiking.climbtogether.vote_activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.hiking.climbtogether.R;
import com.hiking.climbtogether.vote_activity.vote_presenter.VotePresenter;
import com.hiking.climbtogether.vote_activity.vote_presenter.VotePresenterImpl;

public class VoteActivity extends AppCompatActivity implements VoteActivityVu {

    private RecyclerView recyclerView;

    private VoteActivityPresenter presenter;

    private VotePresenter votePresenter;

    private VoteAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vote);
        initPresenter();
        initView();
        presenter.onShowView();
    }

    private void initPresenter() {
        presenter = new VoteActivityPresenterImpl(this);
        votePresenter = new VotePresenterImpl();
    }

    private void initView() {
        Toolbar toolbar = findViewById(R.id.vote_toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.onBackButtonClickListener();
            }
        });

        recyclerView = findViewById(R.id.vote_recycler_view);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));


    }

    @Override
    public void closePage() {
        finish();
    }

    @Override
    public void setRecyclerView() {
        adapter = new VoteAdapter(votePresenter,this);
        recyclerView.setAdapter(adapter);
    }
}
