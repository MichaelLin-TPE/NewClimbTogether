package com.hiking.climbtogether.photo_activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.hiking.climbtogether.R;
import com.hiking.climbtogether.tool.NewImageLoaderManager;

public class PhotoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);

        Intent it = getIntent();
        Bundle bundle = it.getExtras();
        if (bundle != null){
            ImageView ivPhoto = findViewById(R.id.photo_image);
            NewImageLoaderManager.getInstance(PhotoActivity.this).setPhotoUrl(bundle.getString("photoUrl",""),ivPhoto);
        }
        Toolbar toolbar = findViewById(R.id.photo_toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
