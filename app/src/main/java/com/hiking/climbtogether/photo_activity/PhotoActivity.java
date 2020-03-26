package com.hiking.climbtogether.photo_activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.hiking.climbtogether.R;
import com.hiking.climbtogether.tool.NewImageLoaderManager;
import com.hiking.climbtogether.tool.ScaleImage;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

public class PhotoActivity extends AppCompatActivity {

    private ImageLoader imageLoader = ImageLoader.getInstance();
    private DisplayImageOptions options;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);
        initImageLoader();



        Intent it = getIntent();
        Bundle bundle = it.getExtras();
        if (bundle != null){
            ScaleImage ivPhoto = findViewById(R.id.photo_image);

            imageLoader.loadImage(bundle.getString("photoUrl", ""), new ImageLoadingListener() {
                @Override
                public void onLoadingStarted(String imageUri, View view) {

                }

                @Override
                public void onLoadingFailed(String imageUri, View view, FailReason failReason) {

                }

                @Override
                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                    ivPhoto.setImageBitmap(loadedImage);
                }

                @Override
                public void onLoadingCancelled(String imageUri, View view) {

                }
            });
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

    private void initImageLoader() {
        options = new DisplayImageOptions.Builder()
                .showImageForEmptyUri(R.drawable.hiking_logo)
                .showImageOnFail(R.drawable.hiking_logo)
                .showImageOnLoading(R.drawable.hiking_logo)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this)
                .defaultDisplayImageOptions(options).build();
        imageLoader.init(config);
    }
}
