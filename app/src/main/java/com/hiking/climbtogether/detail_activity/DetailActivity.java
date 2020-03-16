package com.hiking.climbtogether.detail_activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.hiking.climbtogether.R;
import com.hiking.climbtogether.db_modle.DataDTO;
import com.hiking.climbtogether.detail_activity.mt_presenter.MtPresenter;
import com.hiking.climbtogether.detail_activity.mt_presenter.MtPresenterImpl;

public class DetailActivity extends AppCompatActivity implements DetailActivityVu{

    private DetailActivityPresenter presenter;

    private DataDTO data;

    private Toolbar toolbar;

    private RecyclerView recyclerView;

    private MtPresenter mtPresenter;

    private DetailAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        initPresenter();
        initBundle();
        initView();

        presenter.onPrepareData(data);
    }

    @Override
    public void closePage() {
        finish();
    }

    private void initView() {
        toolbar = findViewById(R.id.detail_toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.onBackButtonClickListener();
            }
        });
        recyclerView = findViewById(R.id.detail_recycler_view);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,1);
        recyclerView.setLayoutManager(gridLayoutManager);

    }

    private void initBundle() {
        Intent it = getIntent();
        Bundle bundle = it.getExtras();
        if(bundle != null){
            data = (DataDTO) bundle.getSerializable("data");
            Log.i("Michael","有接到資料 : "+data.getName());
        }
    }

    private void initPresenter() {
        mtPresenter = new MtPresenterImpl();
        presenter = new DetailActivityPresenterImpl(this);
    }
    @Override
    public void setToolbarTitle(String name) {
        toolbar.setTitle(name);
    }

    @Override
    public void setRecyclerView(DataDTO data) {
        mtPresenter.setData(data);
        adapter = new DetailAdapter(mtPresenter,this);
        recyclerView.setAdapter(adapter);
    }
}
