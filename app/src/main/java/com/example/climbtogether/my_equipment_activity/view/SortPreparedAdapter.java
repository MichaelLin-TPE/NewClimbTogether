package com.example.climbtogether.my_equipment_activity.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.climbtogether.R;
import com.example.climbtogether.db_modle.EquipmentListDTO;

import java.util.ArrayList;

public class SortPreparedAdapter extends RecyclerView.Adapter<SortPreparedAdapter.ViewHolder> {

    private Context context;

    private ArrayList<EquipmentListDTO> dataArrayList;

    private OnSortPreparedItemClickListener listener;

    public void setOnSortItemClickListener(OnSortPreparedItemClickListener listener){
        this.listener = listener;
    }

    public SortPreparedAdapter(Context context, ArrayList<EquipmentListDTO> dataArrayList) {
        this.context = context;
        this.dataArrayList = dataArrayList;
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

        holder.clickArea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onClick(data.getName(),data.getDescription(),position);
            }
        });

    }

    @Override
    public int getItemCount() {
        return dataArrayList == null ? 0 : dataArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvTitle,tvDescription;
        private ConstraintLayout clickArea;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            clickArea = itemView.findViewById(R.id.my_equipment_click_area);
            tvDescription = itemView.findViewById(R.id.my_equipment_item_description);
            tvTitle = itemView.findViewById(R.id.my_equipment_item_title);
        }
    }

    public interface OnSortPreparedItemClickListener{
        void onClick(String name, String description, int itemPosition);
    }
}
