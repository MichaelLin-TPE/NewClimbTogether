package com.hiking.climbtogether.mountain_collection_activity.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import com.hiking.climbtogether.R;
import com.hiking.climbtogether.db_modle.DataDTO;
import com.makeramen.roundedimageview.RoundedImageView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class PortView extends ConstraintLayout {
    private TextView tvTitle, tvHeight, tvTime;

    private ConstraintLayout clickArea;

    private RoundedImageView ivPhoto;

    private Context context;

    private OnPortViewItemClickListener listener;

    private DisplayImageOptions options;

    private ImageLoader imageLoader = ImageLoader.getInstance();

    public void setOnPortViewItemClickListener(OnPortViewItemClickListener listener){
        this.listener = listener;
    }

    public PortView(Context context) {
        super(context);
        this.context = context;
        Log.i("Michael","new 出 PortView");
        View view = View.inflate(context, R.layout.mt_collection_port_view, this);
        initView(view);
        initImageLoader();
    }

    private void initImageLoader() {
        options = new DisplayImageOptions.Builder()
                .showImageOnFail(R.drawable.empty_photo)
                .showImageForEmptyUri(R.drawable.empty_photo)
                .showImageOnLoading(R.drawable.empty_photo)
                .cacheOnDisk(true)
                .cacheInMemory(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
                .defaultDisplayImageOptions(options).build();
        imageLoader.init(config);
    }

    private void initView(View view) {
        tvTime = view.findViewById(R.id.port_view_text_time);
        tvTitle = view.findViewById(R.id.port_view_text_title);
        tvHeight = view.findViewById(R.id.port_view_text_height);
        ivPhoto = view.findViewById(R.id.port_view_photo);
        clickArea = view.findViewById(R.id.port_view_click_area);
    }


    public void setData(final DataDTO data, boolean isChangeColor, final int position) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd", Locale.TAIWAN);
        tvTime.setText(String.format(Locale.getDefault(), "日期 : %s", sdf.format(new Date(data.getTime()))));
        tvTitle.setText(data.getName());
        tvHeight.setText(data.getHeight());
        clickArea.setBackground(isChangeColor ? ContextCompat.getDrawable(context, R.drawable.mountain_collection_green) : ContextCompat.getDrawable(context,R.drawable.mountain_collection_blue));
        clickArea.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClick(data,position);
            }
        });
        if (data.getphoto() != null){
            ivPhoto.setScaleType(ImageView.ScaleType.FIT_CENTER);
            imageLoader.displayImage(data.getphoto(),ivPhoto,options);
        }else {
            ivPhoto.setScaleType(null);
            Log.i("Michael","照片沒資料");
        }

    }

    public interface OnPortViewItemClickListener{
        void onClick(DataDTO dataDTO,int itemPosition);
    }
}
