package com.hiking.climbtogether.tool;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

import com.hiking.climbtogether.R;
import com.makeramen.roundedimageview.RoundedImageView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

public class ImageLoaderManager  {

    private DisplayImageOptions options;

    private ImageLoader imageLoader = ImageLoader.getInstance();

    private Context context;

    public ImageLoaderManager(Context context){
        this.context = context;
        initImageLoader();
    }

    private void initImageLoader() {
        options = new DisplayImageOptions.Builder()
                .showImageForEmptyUri(R.drawable.empty_photo)
                .showImageOnFail(R.drawable.empty_photo)
                .showImageOnLoading(R.drawable.empty_photo)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
                .defaultDisplayImageOptions(options).build();
        imageLoader.init(config);
    }

    public void setPhotoUrl(String url, RoundedImageView ivImage){
        imageLoader.displayImage(url,ivImage,options);
    }

    public void setPhotoUrl(String url, ImageView ivImage){
        imageLoader.displayImage(url,ivImage,options);
    }
}
