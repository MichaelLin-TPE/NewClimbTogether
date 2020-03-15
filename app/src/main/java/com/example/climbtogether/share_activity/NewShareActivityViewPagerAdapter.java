package com.example.climbtogether.share_activity;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.example.climbtogether.R;
import com.example.climbtogether.tool.ImageLoaderManager;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;
import java.util.Locale;

public class NewShareActivityViewPagerAdapter extends PagerAdapter {

    private ImageLoaderManager loaderManager;

    private Context context;

    private ArrayList<String> downloadUrlArray;

    public NewShareActivityViewPagerAdapter(Context context, ArrayList<String> downloadUrlArray) {
        loaderManager = new ImageLoaderManager(context);
        this.context = context;
        this.downloadUrlArray = downloadUrlArray;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View)object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.share_view_pager_item,null);
        RoundedImageView ivPhoto = view.findViewById(R.id.share_select_photo);
        TextView tvCount = view.findViewById(R.id.share_text_pic_count);
        if (downloadUrlArray.size() == 1){
            tvCount.setVisibility(View.GONE);
        }else {
            tvCount.setVisibility(View.VISIBLE);
            tvCount.setText(String.format(Locale.getDefault(),"%d/%d",position+1,downloadUrlArray.size()));
        }

        Log.i("Michael","圖片下載網址 ; "+downloadUrlArray.get(position));

        loaderManager.setPhotoUrl(downloadUrlArray.get(position),ivPhoto);

        container.addView(view);

        return view;
    }

    @Override
    public int getCount() {
        return downloadUrlArray == null ?  0 : downloadUrlArray.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }
}
