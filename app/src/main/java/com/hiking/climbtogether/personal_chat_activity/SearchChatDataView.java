package com.hiking.climbtogether.personal_chat_activity;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.hiking.climbtogether.R;

public class SearchChatDataView extends ConstraintLayout {

    private EditText edSearchContent;

    private ImageView ivUp,ivDown;

    private OnSearchChatDataListener listener;

    public void setOnSearchChatDataListener(OnSearchChatDataListener listener){
        this.listener = listener;
    }


    public SearchChatDataView(Context context) {
        super(context);
        initView();
    }



    public SearchChatDataView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public SearchChatDataView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }
    private void initView() {
        View view = View.inflate(getContext(), R.layout.search_chat_data_view,this);
        edSearchContent = view.findViewById(R.id.search_edit_content);
        ivUp = view.findViewById(R.id.search_chat_up);
        ivDown = view.findViewById(R.id.search_chat_down);

        edSearchContent.clearFocus();

        edSearchContent.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if (actionId == EditorInfo.IME_ACTION_SEARCH){
                    listener.onTextWatcherListener(edSearchContent.getText().toString());
                }
                return true;
            }
        });
        ivUp.setVisibility(GONE);
        ivDown.setVisibility(GONE);
        ivUp.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onUpClick();
            }
        });
        ivDown.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onDownClick();
            }
        });
    }

    public void setUpDownButtonClickAble() {
        ivUp.setVisibility(VISIBLE);
        ivDown.setVisibility(VISIBLE);
    }

    public void setEditTextEmpty() {
        edSearchContent.setText("");
    }


    public interface OnSearchChatDataListener{
        void onUpClick();

        void onDownClick();

        void onTextWatcherListener(String content);
    }
}
