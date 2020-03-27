package com.hiking.climbtogether.personal_chat_photo_activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.hiking.climbtogether.R;
import com.hiking.climbtogether.photo_activity.PhotoActivity;

import java.util.ArrayList;

public class PersonalPhotoActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_photo);
        Toolbar toolbar = findViewById(R.id.personal_photo_toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        ArrayList<String> imageUrlArray = new ArrayList<>();
        Intent it = getIntent();
        Bundle bundle = it.getExtras();
        if (bundle != null){
            imageUrlArray = bundle.getStringArrayList("imageUrl");
        }

        RecyclerView recyclerView = findViewById(R.id.personal_photo_recycler_view);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,4);

        recyclerView.setLayoutManager(gridLayoutManager);

        PersonalPhotoAdapter adapter = new PersonalPhotoAdapter(this,imageUrlArray);

        recyclerView.setAdapter(adapter);

        adapter.setOnPhotoClickListener(new PersonalPhotoAdapter.OnPhotoClickListener() {
            @Override
            public void onClick(String url) {
                Intent it = new Intent(PersonalPhotoActivity.this, PhotoActivity.class);
                it.putExtra("photoUrl",url);
                startActivity(it);
            }
        });

    }
}
