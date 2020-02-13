package com.example.climbtogether.mountain_collection_activity.view;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import com.example.climbtogether.R;
import com.example.climbtogether.mountain_fragment.db_modle.DataDTO;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class LandView extends ConstraintLayout {

    private ImageView ivIcon;

    private TextView tvTitle,tvTime,tvHeight;

    private ConstraintLayout clickArea;

    private Context context;

    public LandView(Context context) {
        super(context);
        this.context = context;
        View view = View.inflate(context, R.layout.mt_collection_land_view,this);
        initView(view);
    }

    private void initView(View view) {
        ivIcon = view.findViewById(R.id.land_view_photo);
        tvTitle = view.findViewById(R.id.land_view_title);
        tvHeight = view.findViewById(R.id.land_view_height);
        tvTime = view.findViewById(R.id.land_view_time);
        clickArea =  view.findViewById(R.id.land_view_click_area);
    }


    public void setData(DataDTO dataDTO, boolean isColorChange) {
        tvTitle.setText(dataDTO.getName());
        tvHeight.setText(dataDTO.getHeight());
        tvTime.setText(String.format(Locale.getDefault(),"日期 : %s",new SimpleDateFormat("yyyy/MM/dd",Locale.TAIWAN).format(new Date(dataDTO.getTime()))));
        clickArea.setBackground(isColorChange ? ContextCompat.getDrawable(context,R.color.item_yellow) : ContextCompat.getDrawable(context,R.color.item_green));
    }
}
