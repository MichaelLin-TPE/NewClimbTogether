package com.hiking.climbtogether.personal_chat_activity.tools_view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hiking.climbtogether.R;

import java.util.ArrayList;

public class ToolsListView extends ConstraintLayout {

    private RecyclerView recyclerView;

    private OnToolsListClickListener listClickListener;

    public void setOnToolsListClickListener(OnToolsListClickListener listClickListener){
        this.listClickListener = listClickListener;
    }

    public ToolsListView(Context context) {
        super(context);
        initView();
    }



    public ToolsListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public ToolsListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }
    private void initView() {
        View view = View.inflate(getContext(), R.layout.tools_list_view,this);
        recyclerView = view.findViewById(R.id.tool_list_recycler_view);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(),5);
        recyclerView.setLayoutManager(gridLayoutManager);
        ArrayList<ToolsListData> dataArrayList = new ArrayList<>();
        ArrayList<Integer> photoArray = new ArrayList<>();
        photoArray.add(R.drawable.search_tools);
        photoArray.add(R.drawable.picture_tools);
        ArrayList<String> nameArray = new ArrayList<>();
        nameArray.add(getContext().getString(R.string.search));
        nameArray.add(getContext().getString(R.string.picture));
        for (int i = 0 ; i < photoArray.size() ; i ++){
            ToolsListData data = new ToolsListData();
            data.setName(nameArray.get(i));
            data.setPhoto(photoArray.get(i));
            dataArrayList.add(data);
        }
        ToolsListAdapter adapter = new ToolsListAdapter(dataArrayList,getContext());

        recyclerView.setAdapter(adapter);

        adapter.setOnToolsItemClickListener(new ToolsListAdapter.OnToolsItemClickListener() {
            @Override
            public void onClick(String name) {
                listClickListener.onClick(name);
            }
        });
    }


    public interface OnToolsListClickListener{
        void onClick(String name);
    }
}
