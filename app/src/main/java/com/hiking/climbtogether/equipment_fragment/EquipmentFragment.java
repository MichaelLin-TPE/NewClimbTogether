package com.hiking.climbtogether.equipment_fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.hiking.climbtogether.R;
import com.hiking.climbtogether.db_modle.EquipmentDTO;
import com.hiking.climbtogether.db_modle.EquipmentListDTO;
import com.hiking.climbtogether.equipment_fragment.stuff_presenter.StuffPresenter;
import com.hiking.climbtogether.equipment_fragment.stuff_presenter.StuffPresenterImpl;
import com.hiking.climbtogether.login_activity.LoginActivity;
import com.hiking.climbtogether.my_equipment_activity.MyEquipmentActivity;
import com.hiking.climbtogether.tool.FireStoreManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class EquipmentFragment extends Fragment implements EquipmentVu {

    private EquipmentPresenter presenter;

    private StuffPresenter stuffPresenter;

    private Context context;

    private EquipmentAdapter adapter;

    private RecyclerView recyclerView;

    private Button btnGoList;

    private FirebaseFirestore firestore;

    private FireStoreManager manager;

    private FirebaseAuth mAuth;

    private FirebaseUser user;

    private static final String MY_EQUIPMENT = "my_equipment";

    private static final String EQUIPMENT = "equipment";

    private static final String PREPARED = "prepare";

    private ArrayList<String> prepareArrayList;

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
    public void onResume() {
        super.onResume();
        searchPreparedData();
        user = mAuth.getCurrentUser();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initPresenter();
        initFirebase();
        searchPreparedData();
    }

    private void initFirebase() {
        manager = new FireStoreManager();
        firestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
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

    private void searchPreparedData() {
        if (user != null && user.getEmail() != null) {
            prepareArrayList = new ArrayList<>();
            manager.setFirstCollection(MY_EQUIPMENT);
            manager.setFirstDocument(user.getEmail());
            manager.setSecondCollection(PREPARED);
            manager.catchTwoCollectionData(new FireStoreManager.OnConnectingFirebaseListener() {
                @Override
                public void onSuccess(Task<QuerySnapshot> task) {
                    if (getActivity() != null){
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (task.isSuccessful() && task.getResult() != null) {
                                    for (QueryDocumentSnapshot snapshot : task.getResult()) {
                                        prepareArrayList.add(snapshot.getId());
                                    }
                                }
                            }
                        });
                    }
                }
            });
        }
    }


    private void initView(View view) {
        recyclerView = view.findViewById(R.id.equipment_recycler_view);

        recyclerView.setLayoutManager(new LinearLayoutManager(context));

        btnGoList = view.findViewById(R.id.equipment_btn_go_list);

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
        adapter = new EquipmentAdapter(stuffPresenter, context);

        recyclerView.setAdapter(adapter);

        adapter.setOnItemCheckBoxListener(new StuffItemAdapter.OnItemCheckBoxClickListener() {
            @Override
            public void onClick(String name, int sid, String sort) {
                presenter.onItemCheckListener(name, sid, sort);
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
    public void intentToMyEquipmentActivity() {
        if (user != null) {
            Intent it = new Intent(context, MyEquipmentActivity.class);
            context.startActivity(it);
        } else {
            presenter.onNotLoginEvent();
        }

    }




    @Override
    public void intentToLoginActivity() {
        Intent it = new Intent(getActivity(), LoginActivity.class);
        context.startActivity(it);
    }

    @Override
    public void deleteToFirebase(EquipmentDTO data) {
        if (user != null && user.getEmail() != null){
            firestore.collection(MY_EQUIPMENT)
                    .document(user.getEmail())
                    .collection(EQUIPMENT)
                    .document(data.getName())
                    .delete()
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                Log.i("Michael","刪除成功");
                            }
                        }
                    });
            firestore.collection(MY_EQUIPMENT)
                    .document(user.getEmail())
                    .collection(PREPARED)
                    .document(data.getName())
                    .delete()
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                Log.i("Michael","刪除成功");
                            }
                        }
                    });
        }
    }

    @Override
    public void insertToFirebase(EquipmentDTO data) {
        Map<String,Object> map = new HashMap<>();
        map.put("description",data.getDescription());
        if (user != null && user.getEmail() != null){
            boolean isRepeat = false;
            for (String name : prepareArrayList){
                if (name.equals(data.getName())){
                    isRepeat = true;
                    break;
                }
            }
            if (isRepeat){
                return;
            }

            firestore.collection(MY_EQUIPMENT)
                    .document(user.getEmail())
                    .collection(EQUIPMENT)
                    .document(data.getName())
                    .set(map)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                Log.i("Michael","新增成功");
                            }
                        }
                    });
        }

    }
}
