package com.hiking.climbtogether.home_fragment.weather_view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.hiking.climbtogether.R;

import java.util.ArrayList;

public class WeatherDialogAdapter extends RecyclerView.Adapter<WeatherDialogAdapter.ViewHolder> {

    private Context context;

    private ArrayList<String> nameArray;

    private ArrayList<Integer> imageArray;

    private OnWeatherDialogItemClickListener listener;

    public void setOnWeatherDialogItemClickListener(OnWeatherDialogItemClickListener listener){
        this.listener = listener;
    }

    public WeatherDialogAdapter(Context context, ArrayList<String> nameArray,ArrayList<Integer> imageArray) {
        this.context = context;
        this.nameArray = nameArray;
        this.imageArray = imageArray;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.weather_dialog_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.ivIcon.setImageResource(imageArray.get(position));
        holder.tvTitle.setText(nameArray.get(position));
        holder.clickArea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClick(nameArray.get(position),position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return nameArray == null ? 0 : nameArray.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tvTitle;

        private ImageView ivIcon;

        private ConstraintLayout clickArea;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.weather_item_title);
            ivIcon = itemView.findViewById(R.id.weather_item_icon);
            clickArea = itemView.findViewById(R.id.weather_item_click_area);
        }
    }

    public interface OnWeatherDialogItemClickListener{
        void onClick(String name, int position);
    }
}
