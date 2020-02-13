package com.example.climbtogether.member_activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.climbtogether.R;
import com.example.climbtogether.login_activity.LoginActivity;
import com.example.climbtogether.mountain_collection_activity.MountainCollectionActivity;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.Locale;

public class MemberActivity extends AppCompatActivity implements MemberActivityVu {

    private RecyclerView recyclerView;

    private TextView tvNotice,tvEmail;

    private ImageView ivUserIcon;

    private Button btnLogin;

    private FirebaseAuth mAuth;

    private MemberActivityPresenter presenter;

    private FirebaseUser currentUser;

    private MenuItem signOut;

    private GoogleSignInClient signInClient;

    @Override
    protected void onStart() {
        super.onStart();
        currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.member_menu,menu);

        signOut = menu.findItem(R.id.member_sign_out);

        if (currentUser != null){
            signOut.setVisible(true);
        }else {
            signOut.setVisible(false);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.member_sign_out){
            presenter.onSignOutClickListener();
        }

        return super.onOptionsItemSelected(item);
    }

    private void updateUI(FirebaseUser currentUser) {
        if (currentUser != null){
            presenter.onChangeView(false);
        }else {
            presenter.onChangeView(true);
        }
    }
    @Override
    public void changeView(boolean isShow){
        tvNotice.setVisibility(isShow ? View.VISIBLE : View.GONE);
        btnLogin.setVisibility(isShow ? View.VISIBLE : View.GONE);
        ivUserIcon.setVisibility(isShow ? View.GONE : View.VISIBLE);
        tvEmail.setVisibility(isShow ? View.GONE : View.VISIBLE);
        tvEmail.setText(isShow ? "" : String.format(Locale.getDefault(),"目前登入 : %s",currentUser.getEmail()));

        if (signOut != null){
            signOut.setVisible(!isShow);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

    @Override
    public void intentToLoginActivity() {
        Intent it = new Intent(this, LoginActivity.class);
        startActivity(it);
    }

    @Override
    public void signOut() {
        if (mAuth != null){
            GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken(getResources().getString(R.string.default_web_client_id))
                    .requestEmail()
                    .build();
            if (signInClient == null) {
                signInClient = GoogleSignIn.getClient(this, gso);
            }
            mAuth.signOut();
            signInClient.revokeAccess().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    presenter.onChangeView(true);

                }
            });



        }
    }

    @Override
    public void showConfirmSignOutDialog() {
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setMessage(getString(R.string.confirm_to_sign_out))
                .setPositiveButton(getString(R.string.confirm), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                       presenter.onConfirmSignOutClickListener();
                    }
                }).setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).create();
        dialog.show();
    }

    @Override
    public void intentToBrowser(String url) {
        Intent it = new Intent(Intent.ACTION_VIEW);
        it.setData(Uri.parse(url));
        startActivity(it);
    }

    @Override
    public void intentToMtCollectionActivity() {
        Intent it = new Intent(this, MountainCollectionActivity.class);
        startActivity(it);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member);
        //初始化Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        initPresenter();
        initView();
        presenter.onShowRecycler();
    }

    private void initView() {
        Toolbar toolbar = findViewById(R.id.member_toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
                finish();
            }
        });
        recyclerView = findViewById(R.id.member_recycler_view);
        tvNotice = findViewById(R.id.member_text_notice);
        tvEmail = findViewById(R.id.member_text_email);
        ivUserIcon = findViewById(R.id.member_login_icon);
        btnLogin = findViewById(R.id.member_btn_login);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.onLoginButtonClickListener();
            }
        });


    }

    private void initPresenter() {
        presenter = new MemberActivityPresenterImpl(this);
    }

    @Override
    public Context getVuContext() {
        return this;
    }

    @Override
    public void setRecyclerView(ArrayList<String> btnList) {
        ArrayList<Integer> iconArray = new ArrayList<>();
        iconArray.add(R.drawable.flag_top);
        iconArray.add(R.drawable.bed);
        iconArray.add(R.drawable.apply);
        iconArray.add(R.drawable.weather);

        MemberRecyclerViewAdapter adapter = new MemberRecyclerViewAdapter(iconArray,btnList,this);

        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new MemberRecyclerViewAdapter.OnMemberListItemClickListener() {
            @Override
            public void onClick(int itemPosition) {
                presenter.onListItemClickListener(itemPosition);
            }
        });

    }
}
