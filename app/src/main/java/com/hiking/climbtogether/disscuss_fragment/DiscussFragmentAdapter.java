package com.hiking.climbtogether.disscuss_fragment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.hiking.climbtogether.R;

import java.util.ArrayList;

public class DiscussFragmentAdapter extends RecyclerView.Adapter<DiscussFragmentAdapter.ViewHolder> {

    private ArrayList<String> listArrayList;

    private OnDiscussItemClickListener listener;

    private Context context;

    public void setOnDisCussItemClickListener(OnDiscussItemClickListener listener){
        this.listener = listener;
    }

    public DiscussFragmentAdapter (Context context){
        this.context = context;
    }

    public void setData(ArrayList<String> listArrayList){
        this.listArrayList = listArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.discuss_list_item_layout,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {

        if (position == 0){
            holder.clickArea.setBackground(ContextCompat.getDrawable(context,R.drawable.discuss_selector2));
            holder.ivIcon.setImageResource(R.drawable.chat);
        }else {
            holder.clickArea.setBackground(ContextCompat.getDrawable(context,R.drawable.discuss_selector));
            holder.ivIcon.setImageResource(R.drawable.hiking);
        }

        holder.tvTitle.setText(listArrayList.get(position));
        holder.clickArea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClick(listArrayList.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return listArrayList == null ? 0 : listArrayList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{

        private TextView tvTitle;

        private ConstraintLayout clickArea;

        private ImageView ivIcon;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.discuss_item_title);
            clickArea = itemView.findViewById(R.id.discuss_item_click_area);

            ivIcon = itemView.findViewById(R.id.discuss_item_icon);
        }
    }

    public interface OnDiscussItemClickListener{
        void onClick(String listName);
    }
}
