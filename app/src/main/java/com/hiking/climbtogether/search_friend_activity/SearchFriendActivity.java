package com.hiking.climbtogether.search_friend_activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.hiking.climbtogether.R;
import com.hiking.climbtogether.tool.NewImageLoaderManager;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.Locale;

public class SearchFriendActivity extends AppCompatActivity implements SearchFriendVu {

    private SearchFriendPresenter presenter;

    private FirebaseAuth mAuth;

    private FirebaseUser user;

    private FirebaseFirestore firestore;

    private TextView tvUserInfo,tvIsFriendInfo,tvStartInfo;

    private RoundedImageView ivPhoto;

    private ImageView ivIsFriend;

    private Button btnSend;
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.search_menu,menu);
        MenuItem item = menu.findItem(R.id.search_name);
        RelativeLayout itemSearch = (RelativeLayout)item.getActionView();

        EditText edSearch = itemSearch.findViewById(R.id.search_edit_text);

        edSearch.clearFocus();

        edSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if (actionId == EditorInfo.IME_ACTION_SEARCH){
                    presenter.onSearchEvent(edSearch.getText().toString());
                    edSearch.setText("");
                }

                return true;
            }
        });

        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_friend);
        initFirebase();
        initPresenter();
        initView();
    }

    private void initFirebase() {
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        firestore = FirebaseFirestore.getInstance();
    }

    private void initPresenter() {
        presenter = new SearchFriendPresenterImpl(this);
    }

    private void initView() {
        Toolbar toolbar = findViewById(R.id.search_toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tvStartInfo = findViewById(R.id.search_text_start_info);
        ivIsFriend = findViewById(R.id.search_is_friend_icon);
        tvIsFriendInfo = findViewById(R.id.search_user_is_friend_info);
        tvUserInfo = findViewById(R.id.search_user_info);
        ivPhoto = findViewById(R.id.search_user_photo);
        btnSend = findViewById(R.id.search_btn_send);

        btnSend.setVisibility(View.GONE);
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.onSendButtonClickListener();
                btnSend.setEnabled(false);
            }
        });
    }

    @Override
    public void searchUserData(String content) {
        firestore.collection("users")
                .document(content)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful() && task.getResult() != null){
                            DocumentSnapshot snapshot = task.getResult();
                            String photoUrl = (String) snapshot.get("photoUrl");
                            String name = (String) snapshot.get("displayName");
                            String email = (String) snapshot.get("email");
                            if (name != null){
                                searchFriendShip(photoUrl,name,email);
                            }else {
                                presenter.onSearchNoData();
                            }
                        }
                    }
                });
    }

    @Override
    public void showUserInformation(String photoUrl, String name, String email) {



        if (email.equals(user.getEmail())){
            ivPhoto.setVisibility(View.VISIBLE);
            tvUserInfo.setVisibility(View.VISIBLE);
            tvIsFriendInfo.setVisibility(View.VISIBLE);
            ivIsFriend.setVisibility(View.GONE);
            tvStartInfo.setVisibility(View.GONE);
            tvIsFriendInfo.setVisibility(View.VISIBLE);
            btnSend.setVisibility(View.GONE);
            tvIsFriendInfo.setText(getString(R.string.do_not_search_yourself));
        }else {
            ivPhoto.setVisibility(View.VISIBLE);
            tvUserInfo.setVisibility(View.VISIBLE);
            tvIsFriendInfo.setVisibility(View.VISIBLE);
            ivIsFriend.setVisibility(View.GONE);
            tvStartInfo.setVisibility(View.GONE);
            tvIsFriendInfo.setVisibility(View.GONE);
            btnSend.setVisibility(View.VISIBLE);
        }
        NewImageLoaderManager.getInstance(this).setPhotoUrl(photoUrl,ivPhoto);
        tvUserInfo.setText(String.format(Locale.getDefault(),"%s",name));
    }

    @Override
    public void showIsFriendUserInformation(String photoUrl, String name, String email) {
        ivPhoto.setVisibility(View.VISIBLE);
        tvUserInfo.setVisibility(View.VISIBLE);
        tvIsFriendInfo.setVisibility(View.VISIBLE);
        ivIsFriend.setVisibility(View.VISIBLE);
        tvIsFriendInfo.setVisibility(View.VISIBLE);
        tvStartInfo.setVisibility(View.GONE);
        tvIsFriendInfo.setText(getString(R.string.is_friend_already));
        btnSend.setVisibility(View.GONE);
        NewImageLoaderManager.getInstance(this).setPhotoUrl(photoUrl,ivPhoto);
        tvUserInfo.setText(String.format(Locale.getDefault(),"%s",name));
    }

    @Override
    public void showSearChNoDataView() {
        ivIsFriend.setVisibility(View.GONE);
        ivPhoto.setVisibility(View.GONE);
        tvUserInfo.setVisibility(View.GONE);
        tvIsFriendInfo.setVisibility(View.GONE);
        btnSend.setVisibility(View.GONE);
        tvStartInfo.setVisibility(View.VISIBLE);
        tvStartInfo.setText(getString(R.string.search_no_data));
    }

    private void searchFriendShip(String photoUrl, String name, String email) {
        if (user != null && user.getEmail() != null){
            firestore.collection("friendship")
                    .document(user.getEmail())
                    .collection("friend")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful() & task.getResult() != null){
                                boolean isFriend = false;
                                for (QueryDocumentSnapshot snapshot : task.getResult()){
                                    if (email.equals(snapshot.getId())){
                                        isFriend = true;
                                        break;
                                    }
                                }
                                if (isFriend){
                                    presenter.onIsFriendEvent(photoUrl,name,email);
                                }else {
                                    presenter.onIsNotFriendEvent(photoUrl,name,email);
                                }
                            }
                        }
                    });


        }

    }
}
