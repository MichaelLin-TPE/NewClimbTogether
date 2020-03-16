package com.hiking.climbtogether.my_equipment_activity.view;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hiking.climbtogether.R;
import com.hiking.climbtogether.db_modle.EquipmentListDTO;

import java.util.ArrayList;

public class NotPrepareViewHolder extends RecyclerView.ViewHolder {
    private TextView tvTitle;

    private RecyclerView recyclerView;

    private Context context;

    private SortAdapter.OnSortItemClickListener listener;

    public void setOnSortItemClickListener(SortAdapter.OnSortItemClickListener listener){
        this.listener = listener;
    }
    public NotPrepareViewHolder(@NonNull View itemView, Context context) {
        super(itemView);
        this.context = context;
        tvTitle = itemView.findViewById(R.id.sort_tv_title);
        recyclerView = itemView.findViewById(R.id.sort_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.addItemDecoration(new DividerItemDecoration(context,DividerItemDecoration.VERTICAL));
    }

    public void setData(ArrayList<EquipmentListDTO> preparedArrayList) {
        SortAdapter adapter = new SortAdapter(context,preparedArrayList);

        tvTitle.setText(context.getString(R.string.not_prepare));
        recyclerView.setAdapter(adapter);
        adapter.setOnSortItemClickListener(new SortAdapter.OnSortItemClickListener() {
            @Override
            public void onClick(String name,String description, int itemPosition) {
                listener.onClick(name,description,itemPosition);
            }
        });
    }
}
