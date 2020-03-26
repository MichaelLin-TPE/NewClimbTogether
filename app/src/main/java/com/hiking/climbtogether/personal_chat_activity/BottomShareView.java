package com.hiking.climbtogether.personal_chat_activity;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hiking.climbtogether.R;
import com.hiking.climbtogether.my_equipment_activity.FriendData;
import com.hiking.climbtogether.share_activity.NewShareAdapter;

import java.util.ArrayList;

public class BottomShareView extends ConstraintLayout {

    private RecyclerView recyclerView;

    private ImageView ivCancel;


    private OnBottomViewClickListener listener;

    public void setOnShareItemClickListener(OnBottomViewClickListener listener){
        this.listener = listener;
    }

    public BottomShareView(Context context) {
        super(context);

        initView();

    }



    public BottomShareView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public BottomShareView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }
    private void initView() {
        View view = View.inflate(getContext(), R.layout.bottom_share_view,this);
        recyclerView = view.findViewById(R.id.bottom_share_recycler_view);
        ivCancel = view.findViewById(R.id.bottom_share_cancel);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(),4);
        recyclerView.setLayoutManager(gridLayoutManager);

        ivCancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onCancelClick();
            }
        });
    }

    public void setData(ArrayList<FriendData> friendDataArray) {
        if (friendDataArray.size() != 0){
            BottomShareAdapter adapter = new BottomShareAdapter(friendDataArray,getContext());

            recyclerView.setAdapter(adapter);

            adapter.setOnShareItemClickListener(new BottomShareAdapter.OnShareItemClickListener() {
                @Override
                public void onClick(FriendData data) {
                    listener.onUserClick(data);
                }
            });
        }
    }

    public interface OnBottomViewClickListener{
        void onUserClick(FriendData data);
        void onCancelClick();
    }
}
