package com.example.climbtogether.equipment_fragment;

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
import android.widget.Toast;

import com.example.climbtogether.R;
import com.example.climbtogether.db_modle.EquipmentDTO;
import com.example.climbtogether.equipment_fragment.stuff_presenter.StuffPresenter;
import com.example.climbtogether.equipment_fragment.stuff_presenter.StuffPresenterImpl;
import com.example.climbtogether.my_equipment_activity.MyEquipmentActivity;

import java.util.ArrayList;

public class EquipmentFragment extends Fragment implements EquipmentVu{

    private EquipmentPresenter presenter;

    private StuffPresenter stuffPresenter;

    private Context context;

    private EquipmentAdapter adapter;

    private RecyclerView recyclerView;

    private Button btnAddList,btnGoList;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    public static EquipmentFragment newInstance() {


        return new EquipmentFragment();
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

        btnAddList = view.findViewById(R.id.equipment_btn_add_list);
        btnGoList = view.findViewById(R.id.equipment_btn_go_list);

        btnAddList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.onButtonAddListClickListener();
            }
        });
        btnGoList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.onButtonGoListClickListener();
            }
        });

    }


    @Override
    public Context getVuContext() {
        return context;
    }

    @Override
    public void setRecyclerView(ArrayList<EquipmentDTO> bodyArrayList, ArrayList<EquipmentDTO> moveArrayList
            , ArrayList<EquipmentDTO> campArrayList, ArrayList<EquipmentDTO> foodArrayList
            , ArrayList<EquipmentDTO> electronicArrayList, ArrayList<EquipmentDTO> drogArrayList
            , ArrayList<EquipmentDTO> otherArrayList) {

        stuffPresenter.setBodyData(bodyArrayList);
        stuffPresenter.setMoveData(moveArrayList);
        stuffPresenter.setCampData(campArrayList);
        stuffPresenter.setFoodData(foodArrayList);
        stuffPresenter.setElectronicData(electronicArrayList);
        stuffPresenter.setDrogData(drogArrayList);
        stuffPresenter.setOtherData(otherArrayList);
        adapter = new EquipmentAdapter(stuffPresenter,context);

        recyclerView.setAdapter(adapter);

        adapter.setOnItemCheckBoxListener(new StuffItemAdapter.OnItemCheckBoxClickListener() {
            @Override
            public void onClick(String name, int sid, String sort) {
                presenter.onItemCheckListener(name,sid,sort);
            }
        });
    }

    @Override
    public void setUpdateData(ArrayList<EquipmentDTO> bodyArrayList, ArrayList<EquipmentDTO> moveArrayList, ArrayList<EquipmentDTO> campArrayList, ArrayList<EquipmentDTO> foodArrayList, ArrayList<EquipmentDTO> electronicArrayList, ArrayList<EquipmentDTO> drogArrayList, ArrayList<EquipmentDTO> otherArrayList) {
        stuffPresenter.setBodyData(bodyArrayList);
        stuffPresenter.setMoveData(moveArrayList);
        stuffPresenter.setCampData(campArrayList);
        stuffPresenter.setFoodData(foodArrayList);
        stuffPresenter.setElectronicData(electronicArrayList);
        stuffPresenter.setDrogData(drogArrayList);
        stuffPresenter.setOtherData(otherArrayList);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void showAddListSuccessfulMessage(String message) {
        Toast.makeText(context,message,Toast.LENGTH_LONG).show();
    }

    @Override
    public void intentToMyEquipmentActivity() {
        Intent it = new Intent(context, MyEquipmentActivity.class);
        context.startActivity(it);
    }
}
