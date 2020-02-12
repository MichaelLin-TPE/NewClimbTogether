package com.example.climbtogether.home_fragment.news_view;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.climbtogether.R;

import java.util.ArrayList;

public class MountainNewsView extends ConstraintLayout implements MountainNewsVu {

    private Context context;

    private RecyclerView recyclerView;

    private MountainNewsPresenter newsPresenter;

    private MountainNewsRecyclerViewAdapter adapter;

    private Handler handler;

    private ProgressBar progressBar;

    public MountainNewsView(Context context) {
        super(context);
        this.context = context;
        handler = new Handler(context.getMainLooper());

        initPresenter();
        initView();
    }

    //為了在mainThread做
    private void runOnUiThread(Runnable runnable) {
        handler.post(runnable);
    }

    private void initPresenter() {
        newsPresenter = new MountainNewsPresenterImpl(this);
    }

    private void initView() {
        View view = View.inflate(context, R.layout.mountain_news_layout,this);
        progressBar = view.findViewById(R.id.mountain_news_progressbar);
        recyclerView = view.findViewById(R.id.mountain_news_recycler_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(context,DividerItemDecoration.VERTICAL));
    }

    public void showNews() {
        newsPresenter.onPrepareData();

    }

    @Override
    public void showRecyclerView(final ArrayList<String> titleArrayList, final ArrayList<String> locationArrayList, final ArrayList<String> timeArrayList, final ArrayList<String> newsUrlArrayList) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                adapter = new MountainNewsRecyclerViewAdapter(context);
                adapter.setData(titleArrayList,locationArrayList,timeArrayList,newsUrlArrayList);

                recyclerView.setAdapter(adapter);
                adapter.setOnNewsItemClickListener(new MountainNewsRecyclerViewAdapter.OnNewsItemClickListener() {
                    @Override
                    public void onClick(String url) {
                        newsPresenter.onItemClickListener(url);
                    }
                });
        }
        });
    }

    @Override
    public void intentToBrowser(String url) {
        Intent it = new Intent(Intent.ACTION_VIEW);
        it.setData(Uri.parse(url));
        context.startActivity(it);
    }

    @Override
    public void showProgressBar(final boolean isShow) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progressBar.setVisibility(isShow ? VISIBLE : GONE);
            }
        });

    }
}
