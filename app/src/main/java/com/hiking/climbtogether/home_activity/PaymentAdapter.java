package com.hiking.climbtogether.home_activity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.billingclient.api.SkuDetails;
import com.hiking.climbtogether.R;

import java.util.List;
import java.util.Locale;

public class PaymentAdapter extends RecyclerView.Adapter<PaymentAdapter.ViewHolder> {

    private List<SkuDetails> dataArray;

    private Context context;

    private OnPaymentItemClickListener listener;

    public void setOnPaymentItemClickListener(OnPaymentItemClickListener listener){
        this.listener = listener;
    }

    public PaymentAdapter(List<SkuDetails> dataArray, Context context) {
        this.dataArray = dataArray;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.payment_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SkuDetails data = dataArray.get(position);
        holder.tvTitle.setText(String.format(Locale.getDefault(),"%s",data.getTitle()));
        holder.tvDescription.setText(data.getDescription());
        holder.btnBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onPay(data);
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataArray == null ? 0 : dataArray.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tvTitle,tvDescription;

        private Button btnBuy;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.payment_title);
            tvDescription = itemView.findViewById(R.id.payment_description);
            btnBuy = itemView.findViewById(R.id.payment_btn);
        }
    }

    public interface OnPaymentItemClickListener{
        void onPay(SkuDetails data);
    }
}
