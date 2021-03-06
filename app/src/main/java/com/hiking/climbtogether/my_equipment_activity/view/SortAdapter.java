package com.hiking.climbtogether.my_equipment_activity.view;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.hiking.climbtogether.R;
import com.hiking.climbtogether.db_modle.EquipmentListDTO;

import java.util.ArrayList;

public class SortAdapter extends RecyclerView.Adapter<SortAdapter.ViewHolder> {

    private Context context;

    private ArrayList<EquipmentListDTO> dataArrayList;

    private OnSortItemClickListener listener;

    private boolean isCheck;

    private static final int PREPARED = 0;

    private static final int NOT_PREPARE = 1;

    private int type;

    public void setMode(int type){
        this.type = type;
    }

    public void setOnSortItemClickListener(OnSortItemClickListener listener){
        this.listener = listener;
    }

    public SortAdapter(Context context, ArrayList<EquipmentListDTO> dataArrayList) {
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

        if(type == PREPARED){
            holder.tvTitle.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            holder.tvDescription.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        }else {
            holder.tvTitle.getPaint().setFlags(0);
            holder.tvDescription.getPaint().setFlags(0);
        }



        holder.tvTitle.setText(data.getName());
        holder.tvDescription.setText(data.getDescription());
        holder.checkBox.setChecked(isCheck);
        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClick(data.getName(),data.getDescription(),position);
            }
        });
        holder.clickArea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onClick(data.getName(),data.getDescription(),position);
            }
        });
        holder.clickArea.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                listener.onDeleteLongClick(data.getName(),position);

                return false;
            }
        });

    }

    @Override
    public int getItemCount() {
        return dataArrayList == null ? 0 : dataArrayList.size();
    }

    public void setCheck(boolean isCheck) {
        this.isCheck = isCheck;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvTitle,tvDescription;
        private ConstraintLayout clickArea;
        private CheckBox checkBox;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            checkBox = itemView.findViewById(R.id.my_equipment_check_box);
            clickArea = itemView.findViewById(R.id.my_equipment_click_area);
            tvDescription = itemView.findViewById(R.id.my_equipment_item_description);
            tvTitle = itemView.findViewById(R.id.my_equipment_item_title);
        }
    }

    public interface OnSortItemClickListener{
        void onClick(String name,String description,int itemPosition);

        void onDeleteLongClick(String name,int itemPosition);
    }
}
