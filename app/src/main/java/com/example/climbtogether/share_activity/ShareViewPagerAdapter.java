package com.example.climbtogether.share_activity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.example.climbtogether.R;
import com.example.climbtogether.tool.ImageLoaderManager;
import com.makeramen.roundedimageview.RoundedImageView;

public class ShareViewPagerAdapter extends PagerAdapter {

    private Context context;

    private ImageLoaderManager imageLoaderManager;

    public ShareViewPagerAdapter(Context context) {
        this.context = context;
        imageLoaderManager = new ImageLoaderManager(context);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.share_view_pager_item,null);
        RoundedImageView ivPhoto = view.findViewById(R.id.share_select_photo);


        return super.instantiateItem(container, position);
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View)object);
    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }
}
