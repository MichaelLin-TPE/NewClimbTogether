package com.example.climbtogether;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.climbtogether.home_page.HomePageActivity;

public class MainActivity extends AppCompatActivity implements MainActivityVu {

    private MainActivityPresenter mainPresenter;

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initPresenter();
        initActionBar();
        Button btnTest = findViewById(R.id.main_test_btn);
        btnTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainPresenter.onBtnTestClickListener();
            }
        });


    }

    private void initActionBar() {
        toolbar = findViewById(R.id.first_page_toolbar);
        setSupportActionBar(toolbar);
    }

    private void initPresenter() {
        mainPresenter = new MainActivityPresenterImpl(this);
    }

    @Override
    public void intentToHomePageActivity() {
        Intent it = new Intent(this, HomePageActivity.class);
        startActivity(it);
    }
}
