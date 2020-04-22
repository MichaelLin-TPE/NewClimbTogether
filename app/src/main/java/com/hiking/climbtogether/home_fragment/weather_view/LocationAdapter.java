package com.hiking.climbtogether.home_fragment.weather_view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.hiking.climbtogether.R;

import java.util.ArrayList;

public class LocationAdapter extends RecyclerView.Adapter<LocationAdapter.ViewHolder> {

    private ArrayList<String> dataArray;

    private Context context;

    private int userPressIndex;

    private OnLocationItemClickListener listener;

    public void setOnLocationItemClickListener(OnLocationItemClickListener listener){
        this.listener = listener;
    }

    public LocationAdapter(ArrayList<String> dataArray, Context context) {
        this.dataArray = dataArray;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.location_list_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String name = dataArray.get(position);
        holder.tvName.setText(name);

        if (userPressIndex == position){
            holder.tvName.setBackground(ContextCompat.getDrawable(context,R.drawable.btn_selector));
        }else {
            holder.tvName.setBackground(null);
        }
        holder.tvName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClick(name,position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataArray == null ? 0 : dataArray.size();
    }

    public void setUserPressIndex(int userPressIndex) {
        this.userPressIndex = userPressIndex;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tvName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.location_item_name);
        }
    }

    public interface OnLocationItemClickListener{
        void onClick(String name,int itemPosition);
    }
}
