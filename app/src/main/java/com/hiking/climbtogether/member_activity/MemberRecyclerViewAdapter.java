package com.hiking.climbtogether.member_activity;

import android.content.Context;
import android.util.Log;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.nostra13.universalimageloader.utils.L;

import java.util.ArrayList;
import java.util.Locale;

public class MemberRecyclerViewAdapter extends RecyclerView.Adapter<MemberRecyclerViewAdapter.ViewHolder> {

    private ArrayList<String> btnList;

    private Context context;

    private ArrayList<Integer> iconArray;

    private OnMemberListItemClickListener listItemClickListener;

    private FirebaseUser user;

    private int inviteCount;

    private FirebaseFirestore firestore;

    private static final String INVITE_FRIEND = "invite_friend";

    private static final String INVITE = "invite";

    public void setCurrent(FirebaseUser user){
        this.user = user;
    }

    public void setOnItemClickListener(OnMemberListItemClickListener listener){
        this.listItemClickListener = listener;
    }

    public MemberRecyclerViewAdapter(ArrayList<Integer> iconArray,ArrayList<String> btnList , Context context){
        this.btnList = btnList;
        this.context = context;
        this.iconArray = iconArray;
        initFirebase();
    }

    private void initFirebase() {
        firestore = FirebaseFirestore.getInstance();
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.member_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        if (position == 4){
            if (inviteCount != 0){
                holder.tvInvite.setVisibility(View.VISIBLE);
                holder.ivIconNext.setVisibility(View.GONE);
                holder.tvInvite.setText(String.format(Locale.getDefault(),"%d",inviteCount));
            }else {
                holder.tvInvite.setVisibility(View.GONE);
                holder.ivIconNext.setVisibility(View.VISIBLE);
            }
        }else {
            holder.ivIconNext.setVisibility(View.VISIBLE);
            holder.tvInvite.setVisibility(View.GONE);
        }

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

    public void setInviteCount(int inviteCount) {
        this.inviteCount = inviteCount;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private TextView tvTitle;

        private ImageView ivIcon;

        private TextView tvInvite;

        private ConstraintLayout clickArea;

        private ImageView ivIconNext;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.member_item_text_title);
            ivIcon = itemView.findViewById(R.id.member_item_icon);
            clickArea = itemView.findViewById(R.id.member_item_click_area);
            tvInvite = itemView.findViewById(R.id.member_item_text_invite);
            ivIconNext = itemView.findViewById(R.id.member_item_next);
        }
    }

    public interface OnMemberListItemClickListener{
        void onClick(int itemPosition);
    }
}
