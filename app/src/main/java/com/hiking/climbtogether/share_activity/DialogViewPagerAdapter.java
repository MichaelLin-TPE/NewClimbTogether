package com.hiking.climbtogether.share_activity;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.hiking.climbtogether.R;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;
import java.util.Locale;

public class DialogViewPagerAdapter extends PagerAdapter {

    private Context context;

    private ArrayList<Bitmap> bitmapArray;

    public DialogViewPagerAdapter(Context context, ArrayList<Bitmap> bitmapArray) {
        this.context = context;
        this.bitmapArray = bitmapArray;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View)object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {

        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.dialog_view_pager_item,null);
        RoundedImageView ivPhoto = view.findViewById(R.id.dialog_view_pager_photo);

        TextView tvCount = view.findViewById(R.id.dialog_view_pager_pic_count);
        tvCount.setText(String.format(Locale.getDefault(),"%d/%d",position+1,bitmapArray.size()));

        ivPhoto.setImageBitmap(bitmapArray.get(position));
        container.addView(view);
        return view;
    }

    @Override
    public int getCount() {
        return bitmapArray == null ? 0 : bitmapArray.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }
}
