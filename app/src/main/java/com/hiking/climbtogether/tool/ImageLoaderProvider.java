package com.hiking.climbtogether.tool;

import android.graphics.Bitmap;
import android.widget.ImageView;

import com.hiking.climbtogether.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

public class ImageLoaderProvider {

    private static ImageLoaderProvider instance = null;
    private ImageLoader imageLoader = ImageLoader.getInstance();

    private DisplayImageOptions options;

    public static ImageLoaderProvider getInstance(){
        if (instance == null){
            instance = new ImageLoaderProvider();

            return instance;
        }
        return instance;
    }

    public  void initImageLoader() {
        options = new DisplayImageOptions.Builder()
                .showImageForEmptyUri(R.drawable.hiking_logo)
                .showImageOnFail(R.drawable.hiking_logo)
                .showImageOnLoading(R.drawable.hiking_logo)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(ClimbTogetherApplication.getInstance())
                .defaultDisplayImageOptions(options).build();
        imageLoader.init(config);
    }
    public void setImage(String photoUrl , ImageView imageView){
        imageLoader.displayImage(photoUrl,imageView);
    }
}
