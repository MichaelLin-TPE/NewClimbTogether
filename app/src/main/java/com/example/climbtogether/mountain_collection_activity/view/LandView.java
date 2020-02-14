package com.example.climbtogether.mountain_collection_activity.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import com.example.climbtogether.R;
import com.example.climbtogether.mountain_fragment.db_modle.DataDTO;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class LandView extends ConstraintLayout {

    private ImageView ivIcon;

    private TextView tvTitle,tvTime,tvHeight;

    private ConstraintLayout clickArea;

    private Context context;

    private DisplayImageOptions options;

    private PortView.OnPortViewItemClickListener listener;

    private ImageLoader imageLoader = ImageLoader.getInstance();

    public LandView(Context context) {
        super(context);
        this.context = context;
        View view = View.inflate(context, R.layout.mt_collection_land_view,this);
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
        ivIcon = view.findViewById(R.id.land_view_photo);
        tvTitle = view.findViewById(R.id.land_view_title);
        tvHeight = view.findViewById(R.id.land_view_height);
        tvTime = view.findViewById(R.id.land_view_time);
        clickArea =  view.findViewById(R.id.land_view_click_area);
    }


    public void setData(final DataDTO dataDTO, boolean isColorChange) {
        tvTitle.setText(dataDTO.getName());
        tvHeight.setText(dataDTO.getHeight());
        tvTime.setText(String.format(Locale.getDefault(),"日期 : %s",new SimpleDateFormat("yyyy/MM/dd",Locale.TAIWAN).format(new Date(dataDTO.getTime()))));
        clickArea.setBackground(isColorChange ? ContextCompat.getDrawable(context,R.color.item_yellow) : ContextCompat.getDrawable(context,R.color.item_green));
        if (dataDTO.getUserPhoto() != null){
            imageLoader.displayImage(dataDTO.getUserPhoto(),ivIcon,options);
        }else {
            Log.i("Michael","照片沒資料");
        }
        clickArea.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClick(dataDTO);
            }
        });
    }

    public void setOnLandViewItemClickListener(PortView.OnPortViewItemClickListener listener) {
        this.listener = listener;
    }
}
