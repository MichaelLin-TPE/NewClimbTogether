package com.example.climbtogether.my_equipment_activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import com.example.climbtogether.R;
import com.example.climbtogether.db_modle.EquipmentListDTO;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class MyEquipmentActivity extends AppCompatActivity implements MyEquipmentVu{

    private MyEquipmentPresenter presenter;

    private RecyclerView recyclerView;

    private FirebaseFirestore firestore;

    private FirebaseAuth mAuth;

    private FirebaseUser user;

    private static final String MY_EQUIPMENT = "my_equipment";

    private static final String EQUIPMENT = "equipment";

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
        ArrayList<EquipmentListDTO> dataArrayList = new ArrayList<>();

        if (user != null && user.getEmail() != null){
            firestore.collection(MY_EQUIPMENT)
                    .document(user.getEmail())
                    .collection(EQUIPMENT)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful() && task.getResult() != null){
                                for (QueryDocumentSnapshot snapshot : task.getResult()){
                                    EquipmentListDTO data = new EquipmentListDTO();
                                    data.setName(snapshot.getId());
                                    data.setDescription((String)snapshot.get("description"));
                                    dataArrayList.add(data);
                                }
                                if (dataArrayList.size() != 0){
                                    presenter.onCatchDataSuccessful(dataArrayList);
                                }
                            }
                        }
                    });
        }
    }

    private void initView() {
        Toolbar toolbar = findViewById(R.id.my_equipment_toolbar);
        setSupportActionBar(toolbar);
        recyclerView = findViewById(R.id.my_equipment_recycler_ivew);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.onBackButtonClick();
            }
        });
    }

    private void initPresenter() {
        presenter = new MyEquipmentPresenterImpl(this);
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

    }

    @Override
    public void setRecyclerView(ArrayList<EquipmentListDTO> allMyEquipment) {
        MyEquipmentAdapter adapter = new MyEquipmentAdapter(allMyEquipment,this);
        recyclerView.setAdapter(adapter);
    }
}
