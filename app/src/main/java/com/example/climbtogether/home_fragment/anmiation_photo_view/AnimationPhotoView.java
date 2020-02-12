package com.example.climbtogether.home_fragment.anmiation_photo_view;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.climbtogether.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.ArrayList;

public class AnimationPhotoView extends ConstraintLayout {

    private Context context;

    private ImageLoader imageLoader = ImageLoader.getInstance();

    private DisplayImageOptions options;

    private ImageView ivPhoto;

    private int picIndex = 1;

    public AnimationPhotoView(Context context) {
        super(context);
        this.context = context;
        initImagLoader();
        initView();
    }

    private void initView() {
        View view = View.inflate(context,R.layout.animation_photo_layout,this);
        ivPhoto = view.findViewById(R.id.animation_photo);
    }

    private void initImagLoader() {
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.mipmap.ic_launcher)
                .showImageOnFail(R.mipmap.ic_launcher)
                .showImageForEmptyUri(R.mipmap.ic_launcher)
                .cacheOnDisk(true)
                .cacheInMemory(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
                .defaultDisplayImageOptions(options).build();
        imageLoader.init(config);
    }

    public void setPhotoData(ArrayList<String> photoUrlArray) {
        //先給一張預設圖
        imageLoader.displayImage(photoUrlArray.get(0),ivPhoto,options);


        fadeOutAndHideImage(ivPhoto,photoUrlArray);
    }

    //圖片淡出
    private void fadeOutAndHideImage(final ImageView img, final ArrayList<String> photoUrlArray){
        final Animation fadeOut = new AlphaAnimation(1, 0);
        fadeOut.setInterpolator(new AccelerateInterpolator());
        fadeOut.setDuration(3000);


        fadeOut.setAnimationListener(new Animation.AnimationListener()
        {
            public void onAnimationEnd(Animation animation)
            {
                if (picIndex == photoUrlArray.size()){
                    picIndex = 0;
                    imageLoader.displayImage(photoUrlArray.get(picIndex),img,options);
                    picIndex ++;
                    fadeInAndShowImage(img,photoUrlArray);
                }else {
                    imageLoader.displayImage(photoUrlArray.get(picIndex),img,options);
                    picIndex++;
                    fadeInAndShowImage(img,photoUrlArray);
                }

            }
            public void onAnimationRepeat(Animation animation) {}
            public void onAnimationStart(Animation animation) {}
        });

        img.startAnimation(fadeOut);
    }
    //圖片淡入
    private void fadeInAndShowImage(final ImageView img, final ArrayList<String> photoUrlArray){
        Animation fadeIn = new AlphaAnimation(0, 1);
        fadeIn.setInterpolator(new AccelerateInterpolator());
        fadeIn.setDuration(2000);

        fadeIn.setAnimationListener(new Animation.AnimationListener()
        {
            public void onAnimationEnd(Animation animation)
            {
                fadeOutAndHideImage(img,photoUrlArray);
            }
            public void onAnimationRepeat(Animation animation) {}
            public void onAnimationStart(Animation animation) {}
        });

        img.startAnimation(fadeIn);
    }
}
