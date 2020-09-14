package com.hiking.climbtogether.tool;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.hiking.climbtogether.R;

public class ErrorDialogFragment extends DialogFragment {


    private String errorCode;

    private TextView tvContent;

    private Button btnConfirm;

    private OnErrorDialogClickListener listener;

    public  ErrorDialogFragment setOnErrorClickListener(OnErrorDialogClickListener listener){
        this.listener = listener;
        return this;
    }


    public static ErrorDialogFragment newInstance(String errorCode){
        Bundle args = new Bundle();
        ErrorDialogFragment fragment = new ErrorDialogFragment();
        args.putString("error",errorCode);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = View.inflate(getContext(), R.layout.error_dialog_layout,container);
        initView(view);
        return view;
    }

    private void initView(View view) {

        if (getArguments() == null){
            Log.i("Michael","getArgument == null");
            return;
        }
        errorCode = getArguments().getString("error","");
        tvContent = view.findViewById(R.id.error_dialog_content);
        btnConfirm = view.findViewById(R.id.error_dialog_btn_confirm);

        tvContent.setText(errorCode);
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener == null){
                    dismiss();
                    return;
                }
                listener.onClick();
                dismiss();
            }
        });
    }

    public interface OnErrorDialogClickListener{
        void onClick();
    }
}
