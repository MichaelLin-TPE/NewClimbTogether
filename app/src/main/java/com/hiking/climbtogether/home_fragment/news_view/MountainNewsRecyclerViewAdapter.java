package com.hiking.climbtogether.home_fragment.news_view;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.hiking.climbtogether.R;

import java.util.ArrayList;
import java.util.Locale;

public class MountainNewsRecyclerViewAdapter extends RecyclerView.Adapter<MountainNewsRecyclerViewAdapter.ViewHolder> {


    private Context context;

    private ArrayList<String> titleArrayList;
    private ArrayList<String> locationArrayList;
    private ArrayList<String> timeArrayList;
    private ArrayList<String> newsUrlArrayList;

    private OnNewsItemClickListener listener;

    public void setOnNewsItemClickListener(OnNewsItemClickListener listener){
        this.listener = listener;
    }

    public MountainNewsRecyclerViewAdapter(Context context){
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.i("Michael","setCreateView");
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.mountain_news_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        Log.i("Michael","MountainNews OnBindViewHolder");
        holder.tvTitle.setText(titleArrayList.get(position));
        holder.tvLocation.setText(String.format(Locale.getDefault(),"發佈單位 : %s",locationArrayList.get(position)));
        holder.tvTime.setText(String.format(Locale.getDefault(),"發佈日期 : %s",timeArrayList.get(position)));
        holder.clickArea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onClick(newsUrlArrayList.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        Log.i("Michael","資料長度 : "+titleArrayList.size());
        return titleArrayList.size();
    }

    public void setData(ArrayList<String> titleArrayList, ArrayList<String> locationArrayList, ArrayList<String> timeArrayList, ArrayList<String> newsUrlArrayList) {
        this.titleArrayList = titleArrayList;
        this.locationArrayList = locationArrayList;
        this.timeArrayList = timeArrayList;
        this.newsUrlArrayList = newsUrlArrayList;
        Log.i("Michael","MountainNews有資料近來");
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView tvTitle,tvLocation,tvTime;
        private ConstraintLayout clickArea;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.mountain_item_title);
            tvLocation = itemView.findViewById(R.id.mountain_item_location);
            tvTime = itemView.findViewById(R.id.mountain_item_time);
            clickArea = itemView.findViewById(R.id.mountain_item_click_area);
        }
    }
    public interface OnNewsItemClickListener{
        void onClick(String url);
    }
}
