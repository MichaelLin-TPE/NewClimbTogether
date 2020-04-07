package com.hiking.climbtogether.favorite_activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.hiking.climbtogether.R;
import com.hiking.climbtogether.detail_activity.MountainFavoriteData;
import com.hiking.climbtogether.login_activity.LoginActivity;

import java.util.ArrayList;

public class FavoriteActivity extends AppCompatActivity implements FavoriteActivityVu{

    private FavoritePresenter presenter;

    private FirebaseAuth mAuth;

    private FirebaseUser user;

    private FirebaseFirestore firestore;

    private RecyclerView recyclerView;

    private FavoriteAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);
        initPresenter();
        initFirebase();
        initView();
        searchData();
    }

    private void searchData() {
        if (user != null && user.getEmail() != null){
            firestore.collection("favorite")
                    .document(user.getEmail())
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()){
                                DocumentSnapshot snapshot = task.getResult();
                                String json = (String) snapshot.get("json");
                                presenter.onCatchJson(json);
                            }
                        }
                    });
        }else {
            presenter.onNotLoginEvent();
        }

    }

    private void initView() {
        Toolbar toolbar = findViewById(R.id.favorite_toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.onBackButtonClickListener();
            }
        });
        recyclerView = findViewById(R.id.favorite_recycler_view);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,3);
        recyclerView.setLayoutManager(gridLayoutManager);

    }

    private void initFirebase() {
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        firestore = FirebaseFirestore.getInstance();
    }

    private void initPresenter() {
        presenter = new FavoritePresenterImpl(this);
    }

    @Override
    public void closePage() {
        finish();
    }

    @Override
    public void setRecyclerView(ArrayList<MountainFavoriteData> dataArrayList) {
        adapter = new FavoriteAdapter(this,dataArrayList);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void intentToLoginPage() {
        Intent it = new Intent(this, LoginActivity.class);
        startActivity(it);
        finish();
    }
}
