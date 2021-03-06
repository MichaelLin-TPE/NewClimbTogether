package com.hiking.climbtogether.my_equipment_activity.view;

import android.content.Context;
import android.graphics.Paint;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hiking.climbtogether.R;
import com.hiking.climbtogether.db_modle.EquipmentListDTO;

import java.util.ArrayList;

public class PreparedViewHolder extends RecyclerView.ViewHolder {

    private TextView tvTitle;

    private RecyclerView recyclerView;

    private LinearLayout linearLayout;

    private static final int PREPARED = 0;


    private SortPreparedAdapter.OnSortPreparedItemClickListener listener;

    public void setOnSortItemClickListener(SortPreparedAdapter.OnSortPreparedItemClickListener listener){

        this.listener = listener;
    }

    private Context context;
    public PreparedViewHolder(@NonNull View itemView, Context context) {
        super(itemView);
        this.context = context;
        tvTitle = itemView.findViewById(R.id.sort_tv_title);
        recyclerView = itemView.findViewById(R.id.sort_recycler_view);
        linearLayout = itemView.findViewById(R.id.sort_linear);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.addItemDecoration(new DividerItemDecoration(context,DividerItemDecoration.VERTICAL));
    }

    public void setData(ArrayList<EquipmentListDTO> preparedArrayList, boolean isCheck) {
        linearLayout.setBackgroundColor(ContextCompat.getColor(context,R.color.item_blue));
        tvTitle.setText(context.getString(R.string.prepared));
        SortAdapter adapter = new SortAdapter(context,preparedArrayList);
        adapter.setMode(PREPARED);
        adapter.setCheck(isCheck);
        recyclerView.setAdapter(adapter);
        adapter.setOnSortItemClickListener(new SortAdapter.OnSortItemClickListener() {
            @Override
            public void onClick(String name,String description, int itemPosition) {
                listener.onClick(name,description,itemPosition);
            }

            @Override
            public void onDeleteLongClick(String name, int itemPosition) {
                listener.onDeleteLongClick(name,itemPosition);
            }
        });
    }
}
