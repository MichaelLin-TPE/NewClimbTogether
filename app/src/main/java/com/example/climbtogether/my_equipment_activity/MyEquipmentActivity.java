package com.example.climbtogether.my_equipment_activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import com.example.climbtogether.R;
import com.example.climbtogether.db_modle.EquipmentListDTO;

import java.util.ArrayList;

public class MyEquipmentActivity extends AppCompatActivity implements MyEquipmentVu{

    private MyEquipmentPresenter presenter;

    private RecyclerView recyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_equipment);
        initPresenter();
        initView();
        presenter.onPrepareData();
    }

    private void initView() {
        Toolbar toolbar = findViewById(R.id.my_equipment_toolbar);
        setSupportActionBar(toolbar);
        recyclerView = findViewById(R.id.my_equipment_recycler_ivew);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.onBackButtonClick();
            }
        });
    }

    private void initPresenter() {
        presenter = new MyEquipmentPresenterImpl(this);
    }

    @Override
    public void closePage() {
        finish();
    }

    @Override
    public Context getVuContext() {
        return this;
    }

    @Override
    public void setViewMaintain(boolean isShow) {

    }

    @Override
    public void setRecyclerView(ArrayList<EquipmentListDTO> allMyEquipment) {
        MyEquipmentAdapter adapter = new MyEquipmentAdapter(allMyEquipment,this);
        recyclerView.setAdapter(adapter);
    }
}
