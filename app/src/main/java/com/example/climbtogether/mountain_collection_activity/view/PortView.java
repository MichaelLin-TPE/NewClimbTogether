package com.example.climbtogether.mountain_collection_activity.view;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import com.example.climbtogether.R;
import com.example.climbtogether.mountain_fragment.db_modle.DataDTO;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import io.opencensus.trace.propagation.SpanContextParseException;

public class PortView extends ConstraintLayout {
    private TextView tvTitle, tvHeight, tvTime;

    private ConstraintLayout clickArea;

    private ImageView ivPhoto;

    private Context context;

    private int colorCount = 0;

    private ArrayList<Integer> colorArray;

    public PortView(Context context) {
        super(context);
        this.context = context;
        Log.i("Michael","new 出 PortView");
        View view = View.inflate(context, R.layout.mt_collection_port_view, this);
        initView(view);
    }

    private void initView(View view) {
        tvTime = view.findViewById(R.id.port_view_text_time);
        tvTitle = view.findViewById(R.id.port_view_text_title);
        tvHeight = view.findViewById(R.id.port_view_text_height);
        ivPhoto = view.findViewById(R.id.port_view_photo);
        clickArea = view.findViewById(R.id.port_view_click_area);
    }


    public void setData(DataDTO data, boolean isChangeColor) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd", Locale.TAIWAN);
        tvTime.setText(String.format(Locale.getDefault(), "日期 : %s", sdf.format(new Date(data.getTime()))));
        tvTitle.setText(data.getName());
        tvHeight.setText(data.getHeight());

        clickArea.setBackground(isChangeColor ? ContextCompat.getDrawable(context, R.color.item_green) : ContextCompat.getDrawable(context,R.color.item_yellow));









    }
}
