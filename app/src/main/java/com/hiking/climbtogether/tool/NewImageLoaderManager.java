package com.hiking.climbtogether.tool;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

import com.hiking.climbtogether.R;
import com.makeramen.roundedimageview.RoundedImageView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

public class NewImageLoaderManager {
    private static NewImageLoaderManager imageLoaderManager;

    private ImageLoader imageLoader = ImageLoader.getInstance();

    private DisplayImageOptions options;

    private NewImageLoaderManager(Context context){
        initImageLoader(context);
    }

    private void initImageLoader(Context context) {
        options = new DisplayImageOptions.Builder()
                .showImageForEmptyUri(R.drawable.hiking_logo)
                .showImageOnFail(R.drawable.hiking_logo)
                .showImageOnLoading(R.drawable.hiking_logo)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
                .defaultDisplayImageOptions(options).build();
        imageLoader.init(config);
    }

    public static synchronized NewImageLoaderManager getInstance(Context context){
        if (imageLoaderManager == null){
            imageLoaderManager = new NewImageLoaderManager(context);
        }
        return imageLoaderManager;
    }

    public void setPhotoUrl(String url, RoundedImageView ivImage){
        imageLoader.displayImage(url,ivImage,options);
    }

    public void setPhotoUrl(String url, ImageView ivImage){
        imageLoader.displayImage(url,ivImage,options);
    }
}
