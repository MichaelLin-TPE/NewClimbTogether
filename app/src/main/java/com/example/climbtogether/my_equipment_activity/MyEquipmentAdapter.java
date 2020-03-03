package com.example.climbtogether.my_equipment_activity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.climbtogether.R;
import com.example.climbtogether.db_modle.EquipmentListDTO;

import java.util.ArrayList;

public class MyEquipmentAdapter extends RecyclerView.Adapter<MyEquipmentAdapter.ViewHolder> {

    private ArrayList<EquipmentListDTO> dataArrayList;

    private Context context;

    public MyEquipmentAdapter(ArrayList<EquipmentListDTO> dataArrayList, Context context) {
        this.dataArrayList = dataArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.my_equipment_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        EquipmentListDTO data = dataArrayList.get(position);
        holder.tvTitle.setText(data.getName());
        holder.tvDescription.setText(data.getDescription());
    }

    @Override
    public int getItemCount() {
        return dataArrayList == null ? 0 : dataArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tvTitle,tvDescription;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDescription  = itemView.findViewById(R.id.my_equipment_item_description);

            tvTitle = itemView.findViewById(R.id.my_equipment_item_title);
        }
    }
}
