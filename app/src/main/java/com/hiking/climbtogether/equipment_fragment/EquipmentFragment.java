package com.hiking.climbtogether.equipment_fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.hiking.climbtogether.R;
import com.hiking.climbtogether.db_modle.EquipmentDTO;
import com.hiking.climbtogether.equipment_fragment.stuff_presenter.StuffPresenter;
import com.hiking.climbtogether.equipment_fragment.stuff_presenter.StuffPresenterImpl;
import com.hiking.climbtogether.login_activity.LoginActivity;
import com.hiking.climbtogether.my_equipment_activity.MyEquipmentActivity;
import com.hiking.climbtogether.tool.ErrorDialogFragment;
import java.util.ArrayList;


public class EquipmentFragment extends Fragment implements EquipmentVu {

    private EquipmentPresenter presenter;

    private StuffPresenter stuffPresenter;

    private Context context;

    private EquipmentAdapter adapter;

    private RecyclerView recyclerView;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    public static EquipmentFragment newInstance() {


        return new EquipmentFragment();
    }

    @Override
    public void onPause() {
        super.onPause();
        presenter.onClearView();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initPresenter();
    }

    private void initPresenter() {
        presenter = new EquipmentPresenterImpl(this);
        stuffPresenter = new StuffPresenterImpl();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_equipment, container, false);
        initView(view);
        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        presenter.onPrepareData();
    }


    private void initView(View view) {
        recyclerView = view.findViewById(R.id.equipment_recycler_view);

        recyclerView.setLayoutManager(new LinearLayoutManager(context));

        Button btnGoList = view.findViewById(R.id.equipment_btn_go_list);

        btnGoList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.onButtonGoListClickListener();
            }
        });

    }


    @Override
    public void intentToMyEquipmentActivity() {
        Intent it = new Intent(context, MyEquipmentActivity.class);
        context.startActivity(it);
    }


    @Override
    public void intentToLoginActivity() {
        Intent it = new Intent(getActivity(), LoginActivity.class);
        context.startActivity(it);
    }


    @Override
    public void showErrorCode(String errorCode) {

        if (getActivity() == null) {
            return;
        }
        ErrorDialogFragment.newInstance(errorCode).show(getActivity().getSupportFragmentManager(), "dialog");
    }

    @Override
    public void setRecyclerViewList(ArrayList<EquipmentDTO> equipmentArrayList) {
        adapter = new EquipmentAdapter(stuffPresenter, getActivity());
        recyclerView.setAdapter(adapter);
        stuffPresenter.setListData(equipmentArrayList);
        adapter = new EquipmentAdapter(stuffPresenter, context);

        recyclerView.setAdapter(adapter);

        adapter.setOnItemCheckBoxListener(new StuffItemAdapter.OnItemCheckBoxClickListener() {
            @Override
            public void onClick(EquipmentDTO data) {
                presenter.onItemCheckListener(data);
            }
        });
    }

    @Override
    public void updateListData(ArrayList<EquipmentDTO> equipmentArrayList) {
        stuffPresenter.setListData(equipmentArrayList);
        adapter.notifyDataSetChanged();
    }
}
