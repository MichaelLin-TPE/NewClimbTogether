package com.hiking.climbtogether.personal_chat_activity.tools_view;

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

public class ToolsListAdapter extends RecyclerView.Adapter<ToolsListAdapter.ViewHolder> {

    private ArrayList<ToolsListData> dataArrayList;

    private Context context;

    private OnToolsItemClickListener listener;

    public void setOnToolsItemClickListener(OnToolsItemClickListener listener){
        this.listener = listener;
    }

    public ToolsListAdapter(ArrayList<ToolsListData> dataArrayList, Context context) {
        this.dataArrayList = dataArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.tools_list_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ToolsListData data = dataArrayList.get(position);
        holder.ivPhoto.setImageResource(data.getPhoto());
        holder.tvName.setText(data.getName());
        holder.clickArea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClick(data.getName());
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataArrayList == null ? 0 : dataArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView ivPhoto;

        private TextView tvName;

        private ConstraintLayout clickArea;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivPhoto = itemView.findViewById(R.id.tools_item_icon);
            tvName = itemView.findViewById(R.id.tools_item_name);
            clickArea = itemView.findViewById(R.id.tools_item_click_area);
        }
    }

    public interface OnToolsItemClickListener{
        void onClick(String name);
    }
}
