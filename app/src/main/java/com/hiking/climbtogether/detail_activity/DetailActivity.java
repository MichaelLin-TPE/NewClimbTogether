package com.hiking.climbtogether.detail_activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.hiking.climbtogether.MainActivity;
import com.hiking.climbtogether.R;
import com.hiking.climbtogether.db_modle.DataDTO;
import com.hiking.climbtogether.detail_activity.mt_presenter.MtPresenter;
import com.hiking.climbtogether.detail_activity.mt_presenter.MtPresenterImpl;
import com.hiking.climbtogether.login_activity.LoginActivity;
import com.hiking.climbtogether.member_activity.MemberActivity;
import com.hiking.climbtogether.tool.ErrorDialogFragment;
import com.hiking.climbtogether.tool.NewImageLoaderManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DetailActivity extends AppCompatActivity implements DetailActivityVu{

    private DetailActivityPresenter presenter;

    private DataDTO data;

    private Toolbar toolbar;

    private RecyclerView recyclerView;

    private MtPresenter mtPresenter;

    private DetailAdapter adapter;

    private MenuItem ivAddLike;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        Log.i("Michael","onCreateOptionsMenu");

        getMenuInflater().inflate(R.menu.detail_menu,menu);

        ivAddLike = menu.findItem(R.id.detail_like);


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.detail_like){
            boolean isChecked = ivAddLike.isChecked();
            Log.i("Michael","isCheck : "+isChecked);

            presenter.onFavoriteItemClickListener(!isChecked);


        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Log.i("Michael","onCreate");
        initPresenter();
        initBundle();
        initView();

        presenter.onPrepareData(data);
    }



    @Override
    protected void onResume() {
        super.onResume();
        Log.i("Michael","onResume");
    }



    @Override
    public void closePage() {
        finish();
    }

    private void initView() {
        toolbar = findViewById(R.id.detail_toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.onBackButtonClickListener();
            }
        });
        recyclerView = findViewById(R.id.detail_recycler_view);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,1);
        recyclerView.setLayoutManager(gridLayoutManager);

    }

    private void initBundle() {
        Intent it = getIntent();
        Bundle bundle = it.getExtras();
        if(bundle != null){
            data = (DataDTO) bundle.getSerializable("data");
            Log.i("Michael","有接到資料 : "+data.getName());
        }
    }

    private void initPresenter() {
        mtPresenter = new MtPresenterImpl();
        presenter = new DetailActivityPresenterImpl(this);
    }
    @Override
    public void setToolbarTitle(String name) {
        toolbar.setTitle(name);
    }

    @Override
    public void setRecyclerView(DataDTO data) {
        mtPresenter.setData(data);
        adapter = new DetailAdapter(mtPresenter,this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void showFavoriteStatus(boolean isCheck) {
        ivAddLike.setChecked(isCheck);
        ivAddLike.setIcon(isCheck ? R.drawable.like_pressed : R.drawable.like_not_press);
    }


    @Override
    public void showErrorCode(String errorCode) {
        ErrorDialogFragment.newInstance(errorCode).show(getSupportFragmentManager(),"dialog");
    }

    @Override
    public void showToast(String message) {
        Toast.makeText(this,message,Toast.LENGTH_LONG).show();
    }

    @Override
    public void intentToLoginActivity() {
        Intent it = new Intent(this, LoginActivity.class);
        startActivity(it);
        finish();
    }
}
