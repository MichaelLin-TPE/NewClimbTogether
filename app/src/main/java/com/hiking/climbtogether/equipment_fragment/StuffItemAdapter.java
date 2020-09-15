package com.hiking.climbtogether.equipment_fragment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.hiking.climbtogether.R;
import com.hiking.climbtogether.db_modle.EquipmentDTO;

import java.util.ArrayList;

public class StuffItemAdapter extends RecyclerView.Adapter<StuffItemAdapter.ViewHolder> {

    private ArrayList<EquipmentDTO> dataArrayList;
    private OnItemCheckBoxClickListener listener;

    private Context context;

    public void setOnItemCheckBoxClickListener(OnItemCheckBoxClickListener listener){
        this.listener = listener;
    }

    public StuffItemAdapter(ArrayList<EquipmentDTO> dataArrayList, Context context) {
        this.dataArrayList = dataArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.stuff_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        EquipmentDTO data = dataArrayList.get(position);

        holder.tvTitle.setText(data.getName());

        holder.tvDescription.setText(data.getDescription());

        if (data.getCheck().equals("false")){
            holder.checkBox.setChecked(false);
        }else {
            holder.checkBox.setChecked(true);
        }

        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onClick(data);
            }
        });
        holder.clickArea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onClick(data);
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataArrayList == null ? 0 : dataArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tvTitle,tvDescription;

        private CheckBox checkBox;

        private ConstraintLayout clickArea;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            clickArea = itemView.findViewById(R.id.stuff_item_click_area);
            tvTitle = itemView.findViewById(R.id.stuff_item_title);
            tvDescription = itemView.findViewById(R.id.stuff_item_description);
            checkBox = itemView.findViewById(R.id.stuff_item_check_box);
        }
    }

    public interface OnItemCheckBoxClickListener{
        void onClick(EquipmentDTO data);
    }
}
