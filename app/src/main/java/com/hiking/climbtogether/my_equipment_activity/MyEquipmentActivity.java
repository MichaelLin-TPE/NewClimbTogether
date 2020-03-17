package com.hiking.climbtogether.my_equipment_activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hiking.climbtogether.R;
import com.hiking.climbtogether.db_modle.EquipmentListDTO;
import com.hiking.climbtogether.my_equipment_activity.sort_presenter.SortPresenter;
import com.hiking.climbtogether.my_equipment_activity.sort_presenter.SortPresenterImpl;
import com.hiking.climbtogether.my_equipment_activity.view.SortAdapter;
import com.hiking.climbtogether.my_equipment_activity.view.SortPreparedAdapter;
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

    private RecyclerView friendRecyclerView;

    private FriendAdapter friendAdapter;

    private TextView tvNoFriendNotice;

    private ImageView ivNoFriendLogo;

    private int equipmentCount, deleteCount;

    private ArrayList<EquipmentListDTO> notPrepareArrayList;

    private String friendEmail;

    private AlertDialog dialog;

    private ArrayList<String> equipmentIdArray;

    private ArrayList<EquipmentListDTO> preparedArrayList;

    private MyEquipmentAdapter adapter;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.equipment_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.share_icon:
                presenter.onShareButtonClick();
                break;
            case R.id.delete_icon:
                presenter.onDeleteButtonClick();
                break;
        }


        return super.onOptionsItemSelected(item);
    }

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

                                    searchPreparedEquipment(preparedArrayList, notPrepareArrayList);

                                    Log.i("Michael", "尚未準備好的裝備數量 : " + notPrepareArrayList.size());
                                } else {
                                    presenter.onViewMaintain();
                                }
                            }
                        }
                    });
        }
    }

    private void searchPreparedEquipment(ArrayList<EquipmentListDTO> preparedArrayList, ArrayList<EquipmentListDTO> notPrepareArrayList) {
        if (user != null && user.getEmail() != null) {
            firestore.collection(MY_EQUIPMENT)
                    .document(user.getEmail())
                    .collection(PREPARED)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful() && task.getResult() != null) {
                                for (QueryDocumentSnapshot snapshot : task.getResult()) {
                                    EquipmentListDTO data = new EquipmentListDTO();
                                    data.setName(snapshot.getId());
                                    data.setDescription((String) snapshot.get("description"));
                                    preparedArrayList.add(data);
                                }
                                Log.i("Michael", "準備好的裝備數量 : " + preparedArrayList.size());
                                presenter.onCatchDataSuccessful(notPrepareArrayList, preparedArrayList);
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
    public void setRecyclerView(ArrayList<EquipmentListDTO> notPrepareArray, ArrayList<EquipmentListDTO> preparedArray) {
        this.notPrepareArrayList = notPrepareArray;
        this.preparedArrayList = preparedArray;
        sortPresenter.setPreparedData(preparedArray);
        sortPresenter.setNotPrepareData(notPrepareArray);
        adapter = new MyEquipmentAdapter(this, sortPresenter);
        recyclerView.setAdapter(adapter);
        adapter.setOnSortItemClickListener(new SortAdapter.OnSortItemClickListener() {
            @Override
            public void onClick(String name, String description, int itemPosition) {
                for (int i = 0; i < notPrepareArray.size(); i++) {
                    if (name.equals(notPrepareArray.get(i).getName())) {
                        notPrepareArray.remove(i);
                        break;
                    }
                }
                EquipmentListDTO data = new EquipmentListDTO();
                data.setDescription(description);
                data.setName(name);
                preparedArray.add(data);
                sortPresenter.setNotPrepareData(notPrepareArray);
                sortPresenter.setPreparedData(preparedArray);
                notPrepareArrayList = notPrepareArray;
                preparedArrayList = preparedArray;
                adapter.notifyDataSetChanged();
                modifyFirebaseData(name, description);
            }
        });

        adapter.setOnSortPreparedItemClickListener(new SortPreparedAdapter.OnSortPreparedItemClickListener() {
            @Override
            public void onClick(String name, String description, int itemPosition) {
                for (int i = 0; i < preparedArray.size(); i++) {
                    if (name.equals(preparedArray.get(i).getName())) {
                        preparedArray.remove(i);
                        break;
                    }
                }
                EquipmentListDTO data = new EquipmentListDTO();
                data.setName(name);
                data.setDescription(description);
                notPrepareArray.add(data);
                notPrepareArrayList = notPrepareArray;
                preparedArrayList = preparedArray;
                sortPresenter.setPreparedData(preparedArray);
                sortPresenter.setNotPrepareData(notPrepareArray);
                adapter.notifyDataSetChanged();
                modifyFirebaseDataPrepared(name, description);
            }
        });
    }

    @Override
    public void showFriendListDialog() {
        View view = View.inflate(this, R.layout.friend_list_dialog, null);
        friendRecyclerView = view.findViewById(R.id.friend_dialog_recyclerView);
        friendAdapter = new FriendAdapter(this);
        tvNoFriendNotice = view.findViewById(R.id.friend_notice);
        ivNoFriendLogo = view.findViewById(R.id.friend_logo);
        dialog = new AlertDialog.Builder(this)
                .setView(view).create();
        Window window = dialog.getWindow();
        if (window != null) {
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
        dialog.show();
        presenter.onSearchFriendData();

    }

    @Override
    public void searchFriend() {
        if (user != null && user.getEmail() != null) {
            firestore.collection("friendship")
                    .document(user.getEmail())
                    .collection("friend")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful() && task.getResult() != null) {
                                ArrayList<FriendData> dataArrayList = new ArrayList<>();
                                for (QueryDocumentSnapshot snapshot : task.getResult()) {
                                    FriendData data = new FriendData();
                                    data.setEmail((String) snapshot.get("email"));
                                    data.setName((String) snapshot.get("displayName"));
                                    data.setPhoto((String) snapshot.get("photoUrl"));
                                    dataArrayList.add(data);
                                }
                                if (dataArrayList.size() != 0) {
                                    presenter.onCatchFriendDataSuccessful(dataArrayList);
                                } else {
                                    presenter.onHasNoFriendEvent();
                                }
                            }
                        }
                    });
        }

    }

    @Override
    public void setFriendRecyclerView(ArrayList<FriendData> dataArrayList) {
        friendAdapter.setDataArrayList(dataArrayList);
        friendRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        friendRecyclerView.setAdapter(friendAdapter);
        friendAdapter.setOnFriendListClickListener(new FriendAdapter.OnFriendListClickListener() {
            @Override
            public void onClick(String email) {
                presenter.onFriendItemClickListener(email);
                dialog.dismiss();
            }
        });
    }

    @Override
    public void showNoFriendView(boolean isShow) {
        ivNoFriendLogo.setVisibility(isShow ? View.VISIBLE : View.GONE);
        tvNoFriendNotice.setVisibility(isShow ? View.VISIBLE : View.GONE);
    }

    @Override
    public void shareEquipmentListToFriend(String email) {

        equipmentCount = 0;

        this.friendEmail = email;

        Log.i("Michael", "分享的好友Email : " + email);

        shareToFriendList();

        deleteFriendPreparedEquipment();
    }

    private void deleteFriendPreparedEquipment() {
        equipmentIdArray = new ArrayList<>();
        firestore.collection("my_equipment")
                .document(friendEmail)
                .collection("prepare")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful() & task.getResult() != null) {
                            for (QueryDocumentSnapshot snapshot : task.getResult()) {
                                equipmentIdArray.add(snapshot.getId());
                            }
                            if (equipmentIdArray.size() != 0) {
                                Log.i("Michael", "開始刪除 資料長度 : " + equipmentIdArray.size());
                                deleteAllPreparedData();
                            }
                        }
                    }
                });
    }

    private void deleteAllPreparedData() {

        for (String id : equipmentIdArray) {
            firestore.collection("my_equipment")
                    .document(friendEmail)
                    .collection("prepare")
                    .document(id)
                    .delete();
        }
    }

    @Override
    public String getShareSuccessful() {
        return getString(R.string.share_successful);
    }

    @Override
    public void showToast(String message) {

        Toast.makeText(MyEquipmentActivity.this, message, Toast.LENGTH_LONG).show();

    }

    @Override
    public String getPleaseWait() {
        return getString(R.string.please_wait);
    }

    @Override
    public void showDeleteDialog() {
        AlertDialog dialog =new AlertDialog.Builder(this)
                .setTitle(getString(R.string.warning))
                .setMessage(getString(R.string.delete_notice))
                .setPositiveButton(getString(R.string.confirm), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        presenter.onDeleteAllListConfirmClick();
                    }
                }).setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).create();
        dialog.show();
    }

    @Override
    public void deleteAllList() {
        deleteNotPrepareData();
        deletePrepareData();

    }

    @Override
    public void clearView() {
        if (adapter != null){
            sortPresenter.setNotPrepareData(new ArrayList<EquipmentListDTO>());
            sortPresenter.setPreparedData(new ArrayList<EquipmentListDTO>());
            adapter.notifyDataSetChanged();
        }


    }

    @Override
    public String getDeleteDataSuccess() {
        return getString(R.string.delete_success);
    }

    private void deletePrepareData() {
        if (preparedArrayList.size() != 0 && user != null && user.getEmail() != null){
            for (EquipmentListDTO data : preparedArrayList){
                firestore.collection("my_equipment")
                        .document(user.getEmail())
                        .collection("prepare")
                        .document(data.getName())
                        .delete();
            }
        }
    }

    private void deleteNotPrepareData() {
        if (notPrepareArrayList.size() != 0 && user != null && user.getEmail() != null){
            for (EquipmentListDTO data : notPrepareArrayList){
                firestore.collection("my_equipment")
                        .document(user.getEmail())
                        .collection("equipment")
                        .document(data.getName())
                        .delete();
            }
        }
    }

    private void shareToFriendList() {
        if (equipmentCount < notPrepareArrayList.size()) {
            Map<String, Object> map = new HashMap<>();
            map.put("description", notPrepareArrayList.get(equipmentCount).getDescription());
            firestore.collection("my_equipment")
                    .document(friendEmail)
                    .collection("equipment")
                    .document(notPrepareArrayList.get(equipmentCount).getName())
                    .set(map)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Log.i("Michael", "新增成功");
                                equipmentCount++;
                                shareToFriendList();
                            }
                        }
                    });
        } else {
            presenter.onShareSuccessful();
        }
    }

    private void modifyFirebaseDataPrepared(String name, String description) {
        if (user != null && user.getEmail() != null) {
            firestore.collection(MY_EQUIPMENT)
                    .document(user.getEmail())
                    .collection(PREPARED)
                    .document(name)
                    .delete()
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Log.i("Michael", "已刪除 : " + name);
                            }
                        }
                    });
            Map<String, Object> map = new HashMap<>();
            map.put("name", name);
            map.put("description", description);
            firestore.collection(MY_EQUIPMENT)
                    .document(user.getEmail())
                    .collection(EQUIPMENT)
                    .document(name)
                    .set(map)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Log.i("Michael", "此筆 : " + name + " 新增成功");
                            }
                        }
                    });
        }
    }

    private void modifyFirebaseData(String name, String description) {
        if (user != null && user.getEmail() != null) {
            firestore.collection(MY_EQUIPMENT)
                    .document(user.getEmail())
                    .collection(EQUIPMENT)
                    .document(name)
                    .delete()
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Log.i("Michael", "已刪除 : " + name);
                            }
                        }
                    });
            Map<String, Object> map = new HashMap<>();
            map.put("name", name);
            map.put("description", description);
            firestore.collection(MY_EQUIPMENT)
                    .document(user.getEmail())
                    .collection(PREPARED)
                    .document(name)
                    .set(map)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Log.i("Michael", "此筆 : " + name + " 新增成功");
                            }
                        }
                    });
        }
    }
}
