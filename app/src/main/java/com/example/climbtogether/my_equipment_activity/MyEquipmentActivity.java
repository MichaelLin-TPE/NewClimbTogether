package com.example.climbtogether.my_equipment_activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.climbtogether.R;
import com.example.climbtogether.db_modle.EquipmentListDTO;
import com.example.climbtogether.my_equipment_activity.sort_presenter.SortPresenter;
import com.example.climbtogether.my_equipment_activity.sort_presenter.SortPresenterImpl;
import com.example.climbtogether.my_equipment_activity.view.SortAdapter;
import com.example.climbtogether.my_equipment_activity.view.SortPreparedAdapter;
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

public class MyEquipmentActivity extends AppCompatActivity implements MyEquipmentVu {

    private MyEquipmentPresenter presenter;

    private RecyclerView recyclerView;

    private FirebaseFirestore firestore;

    private TextView tvNotice;

    private ImageView ivLogo;

    private FirebaseAuth mAuth;

    private FirebaseUser user;

    private SortPresenter sortPresenter;

    private static final String MY_EQUIPMENT = "my_equipment";

    private static final String EQUIPMENT = "equipment";

    private static final String PREPARED = "prepare";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_equipment);
        initPresenter();
        initView();
        initFirebase();
        searchData();
    }

    private void initFirebase() {
        firestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
    }

    private void searchData() {
        ArrayList<EquipmentListDTO> notPrepareArrayList = new ArrayList<>();
        ArrayList<EquipmentListDTO> preparedArrayList = new ArrayList<>();
        if (user != null && user.getEmail() != null) {
            firestore.collection(MY_EQUIPMENT)
                    .document(user.getEmail())
                    .collection(EQUIPMENT)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful() && task.getResult() != null) {
                                for (QueryDocumentSnapshot snapshot : task.getResult()) {
                                    EquipmentListDTO data = new EquipmentListDTO();
                                    data.setName(snapshot.getId());
                                    data.setDescription((String) snapshot.get("description"));
                                    notPrepareArrayList.add(data);
                                }
                                if (notPrepareArrayList.size() != 0) {
                                    
                                    searchPreparedEquipment(preparedArrayList,notPrepareArrayList);

                                    Log.i("Michael","尚未準備好的裝備數量 : "+notPrepareArrayList.size());
                                } else {
                                    presenter.onViewMaintain();
                                }
                            }
                        }
                    });
        }
    }

    private void searchPreparedEquipment(ArrayList<EquipmentListDTO> preparedArrayList, ArrayList<EquipmentListDTO> notPrepareArrayList) {
        if (user != null && user.getEmail() != null){
            firestore.collection(MY_EQUIPMENT)
                    .document(user.getEmail())
                    .collection(PREPARED)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful() && task.getResult() != null){
                                for (QueryDocumentSnapshot snapshot : task.getResult()) {
                                    EquipmentListDTO data = new EquipmentListDTO();
                                    data.setName(snapshot.getId());
                                    data.setDescription((String) snapshot.get("description"));
                                    preparedArrayList.add(data);
                                }
                                Log.i("Michael","準備好的裝備數量 : "+preparedArrayList.size());
                                presenter.onCatchDataSuccessful(notPrepareArrayList,preparedArrayList);
                            }
                        }
                    });
        }
    }

    private void initView() {
        ivLogo = findViewById(R.id.my_equipment_logo);
        tvNotice = findViewById(R.id.my_equipment_tv_notice);
        Toolbar toolbar = findViewById(R.id.my_equipment_toolbar);
        setSupportActionBar(toolbar);
        recyclerView = findViewById(R.id.my_equipment_recycler_ivew);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.onBackButtonClick();
            }
        });
    }

    private void initPresenter() {
        presenter = new MyEquipmentPresenterImpl(this);
        sortPresenter = new SortPresenterImpl();
    }

    @Override
    public void closePage() {
        finish();
    }

    @Override
    public Context getVuContext() {
        return this;
    }

    @Override
    public void setViewMaintain(boolean isShow) {
        ivLogo.setVisibility(isShow ? View.GONE : View.VISIBLE);
        tvNotice.setVisibility(isShow ? View.GONE : View.VISIBLE);
    }

    @Override
    public void setRecyclerView(ArrayList<EquipmentListDTO> notPrepareArrayList, ArrayList<EquipmentListDTO> preparedArrayList) {
        sortPresenter.setPreparedData(preparedArrayList);
        sortPresenter.setNotPrepareData(notPrepareArrayList);
        MyEquipmentAdapter adapter = new MyEquipmentAdapter(this,sortPresenter);
        recyclerView.setAdapter(adapter);
        adapter.setOnSortItemClickListener(new SortAdapter.OnSortItemClickListener() {
            @Override
            public void onClick(String name,String description, int itemPosition) {
                for (int i = 0 ; i < notPrepareArrayList.size() ; i ++){
                    if (name.equals(notPrepareArrayList.get(i).getName())){
                        notPrepareArrayList.remove(i);
                        break;
                    }
                }
                EquipmentListDTO data = new EquipmentListDTO();
                data.setDescription(description);
                data.setName(name);
                preparedArrayList.add(data);
                sortPresenter.setNotPrepareData(notPrepareArrayList);
                sortPresenter.setPreparedData(preparedArrayList);
                adapter.notifyDataSetChanged();
                modifyFirebaseData(name,description);
            }
        });

        adapter.setOnSortPreparedItemClickListener(new SortPreparedAdapter.OnSortPreparedItemClickListener() {
            @Override
            public void onClick(String name, String description, int itemPosition) {
                for (int i = 0 ; i < preparedArrayList.size() ; i++){
                    if (name.equals(preparedArrayList.get(i).getName())){
                        preparedArrayList.remove(i);
                        break;
                    }
                }
                EquipmentListDTO data = new EquipmentListDTO();
                data.setName(name);
                data.setDescription(description);
                notPrepareArrayList.add(data);
                sortPresenter.setPreparedData(preparedArrayList);
                sortPresenter.setNotPrepareData(notPrepareArrayList);
                adapter.notifyDataSetChanged();
                modifyFirebaseDataPrepared(name,description);
            }
        });
    }

    private void modifyFirebaseDataPrepared(String name, String description) {
        if (user != null && user.getEmail() != null){
            firestore.collection(MY_EQUIPMENT)
                    .document(user.getEmail())
                    .collection(PREPARED)
                    .document(name)
                    .delete()
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                Log.i("Michael","已刪除 : "+name);
                            }
                        }
                    });
            Map<String,Object> map = new HashMap<>();
            map.put("name",name);
            map.put("description",description);
            firestore.collection(MY_EQUIPMENT)
                    .document(user.getEmail())
                    .collection(EQUIPMENT)
                    .document(name)
                    .set(map)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                Log.i("Michael","此筆 : "+name+" 新增成功");
                            }
                        }
                    });
        }
    }

    private void modifyFirebaseData(String name, String description) {
        if (user != null && user.getEmail() != null){
            firestore.collection(MY_EQUIPMENT)
                    .document(user.getEmail())
                    .collection(EQUIPMENT)
                    .document(name)
                    .delete()
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                Log.i("Michael","已刪除 : "+name);
                            }
                        }
                    });
            Map<String,Object> map = new HashMap<>();
            map.put("name",name);
            map.put("description",description);
            firestore.collection(MY_EQUIPMENT)
                    .document(user.getEmail())
                    .collection(PREPARED)
                    .document(name)
                    .set(map)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                Log.i("Michael","此筆 : "+name+" 新增成功");
                            }
                        }
                    });
        }
    }
}
