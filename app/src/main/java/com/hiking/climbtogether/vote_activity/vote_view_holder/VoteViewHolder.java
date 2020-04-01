package com.hiking.climbtogether.vote_activity.vote_view_holder;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.hiking.climbtogether.R;
import com.hiking.climbtogether.vote_activity.VoteActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class VoteViewHolder extends RecyclerView.ViewHolder implements VoteViewHolderVu {

    private EditText editTitle, editTime;

    private ImageView ivDate;

    private VoteViewHolderPresenter presenter;

    private TextView tvSpinner;

    private Button btnCreate;

    private LinearLayout addEditView;

    private Context context;

    private ArrayList<EditText> editItemArray;

    private String topTime;

    private FirebaseFirestore firestore;

    private FirebaseAuth mAuth;

    private FirebaseUser user;

    public VoteViewHolder(@NonNull View itemView, Context context) {
        super(itemView);
        this.context = context;
        presenter = new VoteViewHolderPresenterImpl(this);
        editTime = itemView.findViewById(R.id.create_vote_edit_day_line);
        editTitle = itemView.findViewById(R.id.create_vote_edit_title);
        ivDate = itemView.findViewById(R.id.create_vote_iv_calendar);
        tvSpinner = itemView.findViewById(R.id.create_vote_tv_spinner);
        btnCreate = itemView.findViewById(R.id.create_vote_btn);
        addEditView = itemView.findViewById(R.id.create_vote_add_linear_layout);
        initFirebase();
    }

    private void initFirebase() {
        firestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
    }

    public void showView() {
        tvSpinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                presenter.onSpinnerItemClickListener();

            }
        });
        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String title = editTitle.getText().toString();
                String time = editTime.getText().toString();


                ArrayList<String> itemTextArray = new ArrayList<>();

                if (editItemArray != null){
                    for (EditText editText : editItemArray) {
                        itemTextArray.add(editText.getText().toString());
                    }

                    presenter.onCreateVoteBtnClickListener(itemTextArray,time,title);
                }else {
                    String message = "請選擇投票項目";
                    showErrorCode(message);
                }
            }
        });
        ivDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.onIvDateClickListener();
            }
        });
    }

    @Override
    public void showNumberSelectDialog() {
        String[] numberArray = {"1", "2", "3", "4", "5"};
        AlertDialog dialog = new AlertDialog.Builder(context)
                .setItems(numberArray, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        presenter.onAlertDialogItemClickListener(numberArray, which);
                    }
                }).create();
        dialog.show();


    }

    @Override
    public void setTvSpinnerText(String numberType) {
        tvSpinner.setText(numberType);
    }

    @Override
    public void addEditTextView(String numberStr) {
        addEditView.removeAllViews();
        int number = Integer.parseInt(numberStr);
        editItemArray = new ArrayList<>();
        for (int i = 0; i < number; i++) {
            EditText editText = new EditText(context);
            editText.setHint(String.format("%s", context.getString(R.string.vote_item) + (i + 1)));
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            editText.setLayoutParams(params);
            editItemArray.add(editText);
            addEditView.addView(editText);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void showDatePicker() {
        DatePicker datePicker = new DatePicker(context);
        AlertDialog dialog = new AlertDialog.Builder(context)
                .setView(datePicker).create();
        dialog.show();

        datePicker.setOnDateChangedListener(new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                String monthStr;
                String yearStr;
                String dateStr;
                if ((monthOfYear + 1) < 10) {
                    monthStr = "0" + (monthOfYear + 1);
                } else {
                    monthStr = (monthOfYear + 1) + "";
                }
                if ((dayOfMonth) < 10) {
                    dateStr = "0" + dayOfMonth;
                } else {
                    dateStr = dayOfMonth + "";
                }
                yearStr = year + "";
                topTime = String.format(Locale.getDefault(), "%s/%s/%s", yearStr, monthStr, dateStr);
                editTime.setText(topTime);
                dialog.dismiss();
            }
        });


    }

    @Override
    public void showErrorCode(String message) {
        Toast.makeText(context,message,Toast.LENGTH_LONG).show();
    }

    @Override
    public void saveVoteDataToFirebase(String jsonStr, String title) {
        if (user != null && user.getEmail() != null){
            Map<String,Object> map = new HashMap<>();
            map.put("json", jsonStr);
            map.put("is_day_line",false);
            firestore.collection("vote")
                    .document(title)
                    .set(map)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                ((VoteActivity)context).finish();
                                String message = "發起投票成功";
                                showErrorCode(message);
                            }
                        }
                    });
        }
    }

    @Override
    public String getUserEmail() {
        return user.getEmail();
    }
}
