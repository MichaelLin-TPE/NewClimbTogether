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

    private FirebaseAuth mAuth;

    private FirebaseUser user;

    private StorageReference storage;

    private FirebaseFirestore firestore;

    private boolean isFavorite;

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
            if (user != null){
                presenter.onFavoriteItemClickListener(!isChecked);
            }else {
                presenter.onNoUserEvent();
            }

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Log.i("Michael","onCreate");
        initFirebase();
        initFavoriteData();
        initPresenter();
        initBundle();
        initView();

        presenter.onPrepareData(data);
    }

    private void initFavoriteData() {
        if (user != null && user.getEmail() != null){
            firestore.collection("favorite")
                    .document(user.getEmail())
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()){
                                DocumentSnapshot snapshot = task.getResult();
                                String jsonStr = (String) snapshot.get("json");
                                presenter.onCatchJson(jsonStr);
                            }
                        }
                    });
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("Michael","onResume");
    }

    private void initFirebase() {
        mAuth = FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance().getReference();
        firestore = FirebaseFirestore.getInstance();
        user = mAuth.getCurrentUser();
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
    public void uploadMtPhoto(byte[] photo) {
        if (user != null && user.getEmail() != null){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    StorageReference river = storage.child(user.getEmail() + "/favorite/" + data.getName() + ".jpg");
                    UploadTask task = river.putBytes(photo);

                    task.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            river.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String url = uri.toString();
                                    presenter.onUploadPhotoSuccessful(url);
                                }
                            });
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            e.printStackTrace();
                            Log.i("Michael", "沒照片");
                            Log.i("Michael", e.toString());
                        }
                    });
                }
            }).start();

        }

    }

    @Override
    public void showIsFavorite(boolean isFavorite) {

        ivAddLike.setChecked(isFavorite);

        ivAddLike.setIcon(isFavorite ? R.drawable.like_pressed : R.drawable.like_not_press);
        Log.i("Michael","接到資料isFavorite : "+isFavorite);
    }

    @Override
    public void saveFavoriteMt(String jsonStr) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (user != null && user.getEmail() != null){
                    Map<String,Object> map = new HashMap<>();
                    map.put("json",jsonStr);
                    firestore.collection("favorite")
                            .document(user.getEmail())
                            .set(map)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        Log.i("Michael","更新成功");
                                    }
                                }
                            });
                }
            }
        });
    }

    @Override
    public void IntentToLoginActivity() {
        Intent it = new Intent(this, LoginActivity.class);
        startActivity(it);
        finish();
    }
}
