package com.hiking.climbtogether.equipment_fragment;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hiking.climbtogether.R;
import com.hiking.climbtogether.db_modle.EquipmentDTO;

import java.util.ArrayList;

public class EquipmentViewHolder extends RecyclerView.ViewHolder {

    private RecyclerView recyclerView;

    private TextView tvTitle;

    private Context context;

    private StuffItemAdapter.OnItemCheckBoxClickListener listener;

    public void setOnItemCheckBoxClickListener(StuffItemAdapter.OnItemCheckBoxClickListener listener){
        this.listener = listener;
    }
    public EquipmentViewHolder(@NonNull View itemView, Context context) {
        super(itemView);
        this.context = context;
        recyclerView = itemView.findViewById(R.id.equipment_item_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        tvTitle = itemView.findViewById(R.id.equipment_item_title);
        recyclerView.addItemDecoration(new DividerItemDecoration(context,DividerItemDecoration.VERTICAL));
    }

    public void setData(ArrayList<EquipmentDTO> dataArrayList,String title) {
        StuffItemAdapter adapter = new StuffItemAdapter(dataArrayList,context);
        tvTitle.setText(title);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemCheckBoxClickListener(new StuffItemAdapter.OnItemCheckBoxClickListener() {
            @Override
            public void onClick(EquipmentDTO data) {
                listener.onClick(data);
            }
        });
    }
}
