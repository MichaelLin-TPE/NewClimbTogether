package com.example.climbtogether.mountain_collection_activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.climbtogether.R;
import com.example.climbtogether.login_activity.LoginActivity;
import com.example.climbtogether.mountain_collection_activity.view_presenter.MtPresenter;
import com.example.climbtogether.mountain_collection_activity.view_presenter.MtPresenterImpl;
import com.example.climbtogether.mountain_fragment.db_modle.DataDTO;
import com.example.climbtogether.tool.SpaceItemDecoration;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.example.climbtogether.mountain_collection_activity.view_presenter.MtPresenterImpl.LAND_VIEW;
import static com.example.climbtogether.mountain_collection_activity.view_presenter.MtPresenterImpl.PORT_VIEW;

public class MountainCollectionActivity extends AppCompatActivity implements MountainCollectionVu{

    private MountainCollectionPresenter presenter;

    private RecyclerView recyclerView;

    private Toolbar toolbar;

    private FirebaseAuth mAuth;

    private FirebaseFirestore firestore;

    private TextView tvNotice,tvSpinnerNotice;

    private ImageView ivIcon,ivDropDown;

    private Button btnLogin;

    private FirebaseUser user;

    private MtPresenter mtPresenter;

    private ProgressBar progressBar;

    private MountainCollectionAdapter adapter;

    private Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mountain_collection);
        initFirebase();
        initPresenter();
        initView();
        initSpinner();
    }

    private void initSpinner() {
        spinner = findViewById(R.id.favorite_mt_spinner);
        presenter.onPrepareSpinnerData();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i("Michael","collection onStart");
        user = mAuth.getCurrentUser();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("Michael","collection onResume");
        checkLoginStatus();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (user != null){
            if (adapter != null){
                mtPresenter.setData(new ArrayList<DataDTO>());
                adapter.notifyDataSetChanged();
            }
        }
        Log.i("Michael","collection onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i("Michael","collection onStop");
    }

    private void checkLoginStatus() {
        if (user != null){
            presenter.onUserExist();
        }else {
            presenter.onUserNotExist();
        }
    }

    private void initFirebase() {
        mAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        presenter.onBackPressedListener();
    }

    private void initView() {
        tvSpinnerNotice = findViewById(R.id.favorite_mt_choose_mode);
        ivDropDown = findViewById(R.id.favorite_mt_dropdown);
        progressBar = findViewById(R.id.favorite_mt_progressbar);
        toolbar = findViewById(R.id.favorite_mt_toolbar);

        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.onToolbarNavigationIconClickListener();
            }
        });
        tvNotice = findViewById(R.id.favorite_mt_text_notice);
        ivIcon = findViewById(R.id.favorite_mt_iv_icon);
        btnLogin = findViewById(R.id.favorite_mt_btn_login);
        recyclerView = findViewById(R.id.favorite_mt_recycler_view);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.onBtnLoginClickListener();
            }
        });

        //轉DP
        int pix = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,20,getResources().getDisplayMetrics());

        recyclerView.addItemDecoration(new SpaceItemDecoration(pix));

    }

    private void initPresenter() {
        presenter = new MountainCollectionPresenterImpl(this);
        mtPresenter = new MtPresenterImpl();
    }

    @Override
    public void closePage() {
        finish();
    }

    @Override
    public void setViewMaintain(boolean isShow) {
        tvNotice.setVisibility(isShow ? View.VISIBLE : View.GONE);
        ivIcon.setVisibility(isShow ? View.VISIBLE : View.GONE);
        btnLogin.setVisibility(isShow ? View.VISIBLE : View.GONE);
    }

    @Override
    public void intentToLoginActivity() {
        Intent it = new Intent(this, LoginActivity.class);
        startActivity(it);
    }

    @Override
    public void searchDataFromFirebase() {
        final ArrayList<String> mtNameArray = new ArrayList<>();
        final ArrayList<Long> timeArray = new ArrayList<>();

        String email = user.getEmail();
        if (email != null){
            firestore.collection(email).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()){
                        if (task.getResult() != null){
                            for (QueryDocumentSnapshot snapshot : task.getResult()){
                                mtNameArray.add(snapshot.getId());
                                Map<String,Object> map = snapshot.getData();
                                timeArray.add((Long) map.get("topTime"));
                            }
                            if (mtNameArray.size() != 0 && timeArray.size() != 0){
                                presenter.onSearchDataSuccessFromFirebase(mtNameArray,timeArray);
                            }else {
                                presenter.onSearchNoDataFromFirebase();
                            }
                        }
                    }
                }
            });
        }
    }

    @Override
    public void showSearchNoDataView() {
        //這裡是如果沒有資料的話
        ivIcon.setVisibility(View.VISIBLE);
        tvNotice.setVisibility(View.VISIBLE);
        tvSpinnerNotice.setVisibility(View.GONE);
        ivDropDown.setVisibility(View.GONE);
        spinner.setVisibility(View.GONE);
        tvNotice.setText(getString(R.string.on_favorite_data));
    }

    @Override
    public Context getVuContext() {
        return this;
    }

    @Override
    public void setRecyclerViewData(ArrayList<DataDTO> dataArrayList) {

        mtPresenter.setData(dataArrayList);

        adapter = new MountainCollectionAdapter(this,mtPresenter);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,2);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return mtPresenter.getSpanCount();
            }
        });
        recyclerView.setLayoutManager(gridLayoutManager);


        recyclerView.setAdapter(adapter);
    }

    @Override
    public void showProgressbar(boolean isShow) {
        progressBar.setVisibility(isShow ? View.VISIBLE : View.GONE);
    }

    @Override
    public void setSpinner(ArrayList<String> listArray) {
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this,R.layout.spinner_custom_layout,listArray);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spinner.setAdapter(arrayAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                presenter.onSpinnerItemSelectedListener(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public void setViewType(int position) {
        mtPresenter.setViewType(position);
        checkLoginStatus();
    }

}
