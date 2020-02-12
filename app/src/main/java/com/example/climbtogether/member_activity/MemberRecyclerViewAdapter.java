package com.example.climbtogether.member_activity;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.climbtogether.R;

import java.util.ArrayList;

public class MemberRecyclerViewAdapter extends RecyclerView.Adapter<MemberRecyclerViewAdapter.ViewHolder> {

    private ArrayList<String> btnList;

    private Context context;

    private ArrayList<Integer> iconArray;

    private OnMemberListItemClickListener listItemClickListener;

    public void setOnItemClickListener(OnMemberListItemClickListener listener){
        this.listItemClickListener = listener;
    }

    public MemberRecyclerViewAdapter(ArrayList<Integer> iconArray,ArrayList<String> btnList , Context context){
        this.btnList = btnList;
        this.context = context;
        this.iconArray = iconArray;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.member_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.tvTitle.setText(btnList.get(position));
        holder.ivIcon.setImageDrawable(ContextCompat.getDrawable(context,iconArray.get(position)));
        holder.clickArea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listItemClickListener.onClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return btnList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private TextView tvTitle;

        private ImageView ivIcon;

        private ConstraintLayout clickArea;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.member_item_text_title);
            ivIcon = itemView.findViewById(R.id.member_item_icon);
            clickArea = itemView.findViewById(R.id.member_item_click_area);
        }
    }

    public interface OnMemberListItemClickListener{
        void onClick(int itemPosition);
    }
}
