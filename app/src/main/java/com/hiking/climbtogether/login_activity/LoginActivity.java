package com.hiking.climbtogether.login_activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.hiking.climbtogether.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.hiking.climbtogether.tool.UserDataManager;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity implements LoginActivityVu {

    private LoginActivityPresenter presenter;

    private EditText editAccount, editPassword;

    private Button btnLogin, btnRegister;

    private SignInButton btnGoogle;

    private FirebaseAuth mAuth;

    private AlertDialog dialogRegister;

    private TextView tvProcessing;

    private Button btnDialogRegister;

    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();

    private GoogleSignInClient signInClient;

    private static final int RC_SIGN_IN = 100;

    private ProgressBar progressBar;

    private UserDataManager userDataManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //初始化Firebase
        userDataManager = new UserDataManager(this);
        mAuth = FirebaseAuth.getInstance();
        initGoogleOptions();
        initPresenter();
        initView();
    }

    private void initGoogleOptions() {
        GoogleSignInOptions options = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        signInClient = GoogleSignIn.getClient(this, options);
    }

    private void initView() {
        Toolbar toolbar = findViewById(R.id.login_toolbar);

        setSupportActionBar(toolbar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                finish();
            }
        });

        editAccount = findViewById(R.id.login_edit_email);
        editPassword = findViewById(R.id.login_edit_password);
        btnLogin = findViewById(R.id.login_btn);
        btnRegister = findViewById(R.id.login_register);
        btnGoogle = findViewById(R.id.login_google_login);
        progressBar = findViewById(R.id.login_progressbar);

        setGooglePlusButton(btnGoogle);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = editAccount.getText().toString();
                String password = editPassword.getText().toString();

                presenter.onLoginButtonClickListener(email, password);
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.onRegisterButtonClickListener();
            }
        });

        btnGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.onGoogleSignInClickListener();
            }
        });
    }

    //設置SignInButton的字樣
    private void setGooglePlusButton(SignInButton btnGoogle) {
        for (int i = 0; i < btnGoogle.getChildCount(); i++) {
            View view = btnGoogle.getChildAt(i);
            if (view instanceof TextView) {
                TextView text = (TextView) view;
                text.setText(getString(R.string.sign_in_with_google));
            }
        }
    }

    private void initPresenter() {
        presenter = new LoginActivityPresenterImpl(this);
    }

    @Override
    public void showLoginFail(String failMessage) {
        Toast.makeText(this, failMessage, Toast.LENGTH_LONG).show();
    }

    @Override
    public void closePage() {
        finish();
    }

    @Override
    public void signIn(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            saveUserData(email);
                            presenter.onLoginSuccessful();
                        } else {
                            if (task.getException() != null) {
                                String loginFailMessage = getString(R.string.login_fail) + task.getException().toString();
                                presenter.onLoginFail(loginFailMessage);
                            }

                        }
                    }
                });

    }

    private void saveUserData(String email) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                firestore.collection("users")
                        .document(email)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful() && task.getResult() != null){
                                    DocumentSnapshot snapshot = task.getResult();
                                    userDataManager.saveUserData(email,(String)snapshot.get("displayName"),(String)snapshot.get("photoUrl"));
                                }
                            }
                        });
            }
        }).start();
    }

    @Override
    public void showRegisterDialog() {
        View view = View.inflate(this, R.layout.register_custom_dialog, null);
        final EditText editEmail = view.findViewById(R.id.register_edit_email);
        final EditText editPassword = view.findViewById(R.id.register_edit_password);
        final EditText editDisplayName = view.findViewById(R.id.register_edit_display_name);
        tvProcessing = view.findViewById(R.id.register_text_processing);
        btnDialogRegister = view.findViewById(R.id.register_btn);
        dialogRegister = new AlertDialog.Builder(this)
                .setView(view).create();
        dialogRegister.show();
        btnDialogRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = editEmail.getText().toString();
                String password = editPassword.getText().toString();
                String displayName = editDisplayName.getText().toString();
                long currentTime = System.currentTimeMillis();
                presenter.onDialogRegisterButtonClickListener(email, password, displayName, currentTime);
            }
        });
    }

    @Override
    public void registerAccount(final String email, String password, final String displayName, final long currentTime) {
        btnDialogRegister.setEnabled(false);
        tvProcessing.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    //取得UID
                    FirebaseUser firebaseUser = mAuth.getCurrentUser();
                    Map<String, Object> user = new HashMap<>();
                    user.put("email", email);
                    user.put("displayName", displayName);
                    user.put("currentTime", currentTime);
                    user.put("photoUrl","");
                    user.put("token","");
                    if (firebaseUser != null) {
                        user.put("uid", firebaseUser.getUid());
                    }
                    firestore.collection("users")
                            .document(email)
                            .set(user)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.i("Michael", "Create a data successful");
                                    dialogRegister.dismiss();
                                    tvProcessing.setVisibility(View.GONE);
                                    presenter.onRegisterSuccessful();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            presenter.onLoginFail(getString(R.string.login_fail) + e.toString());
                            btnDialogRegister.setEnabled(true);
                            tvProcessing.setVisibility(View.GONE);
                        }
                    });
                } else {
                    if (task.getException() != null) {
                        btnDialogRegister.setEnabled(true);
                        tvProcessing.setVisibility(View.GONE);
                        presenter.onLoginFail(getString(R.string.login_fail) + task.getException().toString());
                    }
                }
            }
        });
    }

    @Override
    public void signInWithGmail() {
        Intent it = signInClient.getSignInIntent();
        startActivityForResult(it, RC_SIGN_IN);
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                if (account != null){
                    presenter.onViewChangeByGoogleSignIn();
                    firbaseAuthWithGoogle(account);
                }
            } catch (Exception e) {
                presenter.onLoginFail(getString(R.string.google_login_fail) + e.toString());
                Log.i("Michael", "Google 登入失敗 : " + e.toString());
            }
        }
    }

    private void firbaseAuthWithGoogle(GoogleSignInAccount account) {
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            if (user != null) {
                                searchCloundFirestore(user);
                            }
                        } else {
                            Log.i("Michael","有帳號的狀況下跑到這裡來");
                        }
                    }
                });
    }

    private void searchCloundFirestore(final FirebaseUser user) {
        Log.i("Michael","firestore有做到");
        Log.i("Michael","UserEmail : "+user.getEmail());
        if (user.getEmail() != null){
            firestore.collection("users").document(user.getEmail()).get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot documentSnapshot = task.getResult();
                                if (documentSnapshot != null) {
                                    if (documentSnapshot.exists()) {
                                        Map<String, Object> data = documentSnapshot.getData();
                                        if (data != null) {
                                            String email = (String) data.get("email");
                                            Log.i("Michael", "已有資料 email : "+email);
                                            presenter.onGoogleSignInSuccess();
                                        }
                                    }else {
                                        Log.i("Michael","document 不存在");
                                        long currentTime = System.currentTimeMillis();
                                        Map<String,Object> data = new HashMap<>();
                                        data.put("email",user.getEmail());
                                        data.put("displayName",user.getDisplayName());
                                        data.put("uid",user.getUid());
                                        data.put("currentTime",currentTime);
                                        presenter.onCloudFirestoreRegisterEvent(data,user.getEmail());
                                    }
                                }else {
                                    Log.i("Michael","document 沒資料做這");
                                }
                            } else {
                                Log.i("Michael","沒資料做這");
                            }
                        }
                    });
        }

    }

    @Override
    public void createFirestoreDocument(Map<String, Object> data,String email) {
        firestore.collection("users").document(email)
                .set(data)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            presenter.onRegisterSuccessful();
                        }
                    }
                });
    }

    @Override
    public void showAllButtonEnable(boolean isEnable) {
        btnLogin.setEnabled(isEnable);
        btnRegister.setEnabled(isEnable);
        btnGoogle.setEnabled(isEnable);
        progressBar.setVisibility(!isEnable ? View.VISIBLE : View.GONE);
    }
}
